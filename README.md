# 빌드하는 법

## git clone (gitlab)
git clone http://git.drun-alpaca.com/alpaca-drun/apply_aws_web.git

## clone한 디렉토리로 이동
cd apply_aws_web/

## mongo directory 형성
mkdir -p ./mongo/{data,logs,config,express}
### mongo directory 권한 변경 (하위 디렉토리까지)
sudo chown -R 998:995 mongo/

## docker install (설치되어 있다면 pass)
### docker 및 compose install script 파일 실행.
sh install_docker.sh
### docker group으로 이동
newgrp docker

### env 파일 형식 복사
cp .env_example .env
### env 파일 작성
vi .env