from flask import Flask, render_template, request, redirect, url_for, flash, session
# from flask_wtf.csrf import CSRFProtect
from pymongo import MongoClient
from bson.objectid import ObjectId
import datetime
import os
from dotenv import load_dotenv
from datetime import datetime
import json

user_file = open('./app/user_data.json')
user_json = json.load(user_file)
load_dotenv()

LOG_PATH = os.getenv('LOG_PATH')
OBJECT_PATH = os.getenv('OBJECT_PATH')
MONGO_USER = os.getenv('MONGO_USER')
MONGO_PASSWORD = os.getenv('MONGO_PASSWORD')
MONGO_PORT = os.getenv('MONGO_PORT')

app = Flask(__name__)
app.secret_key = 'your_secret_key'

# 로그 설정
def write_log(log_entry):
    today = datetime.now().strftime('%Y%m%d')[2:]
    with open(f'{LOG_PATH}/{today}.log', 'a') as log_file:
        log_file.write(json.dumps(log_entry) + '\n')

def whats_your_name(ip, auth):
    try:
        if auth == True:
            user_name = '관리자_' + user_json[f'{ip}']
        else:
            user_name = user_json[f'{ip}']
    except:
        user_name = 'WHO?'
        
    return user_name

# MongoDB 연결 설정
client = MongoClient(f'mongodb://{MONGO_USER}:{MONGO_PASSWORD}@aaw-mongodb:{MONGO_PORT}/')
db = client['apply_aws_web']
collection_posts = db['posts']
collection_login = db['login']
collection_tags = db['tags']
collection_comments = db['comments']

# 관리자 계정 설정
ADMIN_USERNAME = os.getenv('ADMIN_USERNAME')
ADMIN_PASSWORD = os.getenv('ADMIN_PASSWORD')

app.config['UPLOAD_FOLDER'] = OBJECT_PATH

@app.before_request
def before_request():
    request.client_ip = request.remote_addr
    if 'authenticated' not in session:
        session['authenticated'] = False
    
@app.route('/')
def index():
    return render_template('index.html');

@app.route('/login', methods=['GET', 'POST'])
def admin_login():
    if not session.get('authenticated'):
        if request.method == 'POST':
            username = request.form['username']
            password = request.form['password']

            # ID와 비밀번호가 일치하는지 확인
            if username == ADMIN_USERNAME and password == ADMIN_PASSWORD:
                session['authenticated'] = True
                user_name = whats_your_name(request.client_ip, session['authenticated'])
                log_entry = {
                    'case' : 'AAW-01',
                    'access': True,
                    'date': datetime.now().strftime('%Y-%m-%d %H:%M:%S'),
                    'write_id': username,
                    'write_pw': password,
                    'user': user_name,
                    'ip': request.client_ip
                }
                collection_login.insert_one(log_entry)
                return redirect(url_for('admin'))
            else:
                user_name = whats_your_name(request.client_ip, session['authenticated'])
                log_entry = {
                    'case' : 'AAW-01',
                    'access': False,
                    'date': datetime.now().strftime('%Y-%m-%d %H:%M:%S'),
                    'write_id': username,
                    'write_pw': password,
                    'user': user_name,
                    'ip': request.client_ip
                }
                collection_login.insert_one(log_entry)
                return render_template('admin_login.html', alert_notauth=True)
        return render_template('admin_login.html')
    else:
        return redirect(url_for('admin'))

@app.route('/admin')
def admin():
    if not session.get('authenticated'):
        return redirect(url_for('admin_login', alert_notauth=True))
    
    return render_template('admin.html')
    
@app.route('/logout')
def logout():
    session['authenticated'] = False
    # return redirect(url_for('index', alert_logout=True))
    return redirect(url_for('index'))

# 글 작성
@app.route('/create_post', methods=['GET', 'POST'])
def create_post():
    if request.method == 'POST':
        user_name = whats_your_name(request.client_ip, session['authenticated'])
        title = request.form['title']
        service = request.form['service']
        reason = request.form['reason']
        start_date = request.form['start_date']
        end_date = request.form['end_date']
        date = start_date + ' ~ ' + end_date
        status = '요청'  # 초기 상태는 '요청'으로 설정
        post = {
            "case": 'AAW-02',
            "title": title,
            "service": service,
            "reason": reason,
            "start_date": start_date,
            "end_date": end_date,
            "date": date,
            "status": status,
            "created_at": datetime.utcnow(),
            "author": user_name,
            'ip': request.client_ip
        }
        
        collection_posts.insert_one(post)
        # return redirect(url_for('create_post', alert_create=True))
        return redirect(url_for('posts', page=1, alert_create=True))
    return render_template('create_post.html')


# AWS 게시글 목록 - 페이징
@app.route('/posts', defaults={'page': 1})
@app.route('/posts/<int:page>')
def posts(page):
    posts_per_page = 8
    skip = (page - 1) * posts_per_page
    all_posts = collection_posts.find().sort("created_at", -1).skip(skip).limit(posts_per_page)
    total_posts = collection_posts.count_documents({})
    total_pages = (total_posts + posts_per_page - 1) // posts_per_page
    return render_template('posts.html', posts=all_posts, page=page, total_pages=total_pages)

# 글 조회
@app.route('/posts/<post_id>')
def view_post(post_id):
    post = collection_posts.find_one({"_id": ObjectId(post_id)})
    comments = list(collection_comments.find({"post_id": ObjectId(post_id)}))
    
    if not post:
        return redirect(url_for('posts', alert_nonepost0=True))

    return render_template('view_post.html', post=post, comments=comments)

# 댓글
@app.route('/posts/<post_id>/comments', methods=['POST'])
def add_comment(post_id):
    post = collection_posts.find_one({"_id": ObjectId(post_id)})

    # 폼에서 데이터를 받습니다.
    category = request.form['categoryInput']
    comment = request.form['commentInput']
    
    if not comment or not category:
        return redirect(url_for('view_post', post_id=post_id))  # 폼이 잘못된 경우 다시 리디렉션
    
    user_name = whats_your_name(request.client_ip, session['authenticated'])
    comment_data = {
        "category": category,
        "comment": comment,
        "author": user_name,
        "created_at": datetime.utcnow(),
        "post_id": ObjectId(post_id),
        'ip': request.client_ip
    }
    
    collection_comments.insert_one(comment_data)

    # 댓글이 추가된 페이지로 리디렉션
    return redirect(url_for('view_post', post_id=post_id))

# 댓글 삭제
@app.route('/posts/<post_id>/comments/<comment_id>/delete', methods=['POST'])
def delete_comment(post_id, comment_id):
    # 댓글 삭제 처리
    result = collection_comments.delete_one({"_id": ObjectId(comment_id)})

    if result.deleted_count == 1:
        return redirect(url_for('view_post', post_id=post_id))
    else:
        return {"error": "댓글을 삭제하는 데 실패했습니다."}, 400

# 글 수정
@app.route('/edit_post/<post_id>', methods=['GET', 'POST'])
def edit_post(post_id):
    post = collection_posts.find_one({"_id": ObjectId(post_id)})
    if not post:
        return redirect(url_for('posts', alert_nonepost1=True))

    if request.method == 'POST':
        # 글 수정 처리
        title = request.form['title']
        service = request.form['service']
        reason = request.form['reason']
        start_date = request.form['start_date']
        end_date = request.form['end_date']
        status = request.form['status']
        user_name = whats_your_name(request.client_ip, session['authenticated'])
        
        collection_posts.update_one(
            {"_id": ObjectId(post_id)},
            {"$set": {
                "title": title, 
                "status": status,
                "service": service, 
                "reason": reason, 
                "start_date": start_date,
                "end_date": end_date,
                "username": user_name,
                'ip': request.client_ip
            }}
        )
        return redirect(url_for('view_post', post_id=post_id, alert_modify=True))

    return render_template('edit_post.html', post=post)

# 글 삭제
@app.route('/delete_post/<post_id>', methods=['POST'])
def delete_post(post_id):
    post = collection_posts.find_one({"_id": ObjectId(post_id)})
    if not post:
        return redirect(url_for('posts', alert_nonepost2=True))

    collection_posts.delete_one({"_id": ObjectId(post_id)})
    return redirect(url_for('posts', alert_delete=True))

if __name__ == '__main__':
    app.run(debug=True, port=1541, host='0.0.0.0')
