
document.addEventListener("DOMContentLoaded", function() {
    const actionsDiv = document.querySelector('.actions');
    const isAuthenticated = actionsDiv.dataset.authenticated === "True";

    if (!isAuthenticated) {
        const editButton = document.querySelector('.edit-button');
        const deleteButton = document.querySelector('.delete-button');

        if (editButton) editButton.style.display = 'none';
        if (deleteButton) deleteButton.style.display = 'none';
    }
});

document.addEventListener("DOMContentLoaded", function() {
    // URL의 query string을 확인합니다.
    const urlParams = new URLSearchParams(window.location.search);
    const alert_create = urlParams.get('alert_create');
    const alert_modify = urlParams.get('alert_modify');
    const alert_delete = urlParams.get('alert_delete');
    const alert_logout = urlParams.get('alert_logout');
    const alert_comment = urlParams.get('alert_comment');
    const alert_nonepost0 = urlParams.get('alert_nonepost0');
    const alert_nonepost1 = urlParams.get('alert_nonepost1');
    const alert_nonepost2 = urlParams.get('alert_nonepost2');
    const alert_notauth = urlParams.get('alert_notauth');

    if (alert_create) {
        alert('신청 완료!');
        window.location.href = postsUrl;
    }
    if (alert_modify) {
        alert('수정 완료!');
        window.location.href = postsUrl;
    }
    if (alert_delete) {
        alert('삭제 완료!');
        window.location.href = postsUrl;
    }
    if (alert_logout) {
        alert('로그아웃!');
        window.location.href = postsUrl;
    }
    if (alert_comment) {
        alert('댓글 작성 완료!');
        window.location.href = postsUrl;
    }
    if (alert_nonepost0) {
        alert('글 조회 못하겠쮜!');
        window.location.href = postsUrl;
    }
    if (alert_nonepost1) {
        alert('글 수정 못하겠쮜!');
        window.location.href = postsUrl;
    }
    if (alert_nonepost2) {
        alert('글 삭제 못하겠쮜!');
        window.location.href = postsUrl;
    }
    if (alert_notauth) {
        alert('너 누구야!');
        window.location.href = postsUrl;
    }
});

document.addEventListener('DOMContentLoaded', function() {
    // 오늘 날짜와 6일 후의 날짜를 계산
    const today = new Date();
    const sixDaysLater = new Date(today);
    sixDaysLater.setDate(today.getDate() + 6);

    // 날짜를 MM/DD/YYYY 형식으로 포맷팅
    const formatDate = (date) => {
        const month = (date.getMonth() + 1).toString().padStart(2, '0');
        const day = date.getDate().toString().padStart(2, '0');
        const year = date.getFullYear();
        return `${month}/${day}/${year}`;
    };

    // 각각의 입력 필드에 날짜 설정
    document.getElementById('start_date').value = formatDate(today);
    document.getElementById('end_date').value = formatDate(sixDaysLater);

    // Date Picker 초기화
    $(function() {
        $('#start_date').daterangepicker({
            singleDatePicker: true,
            showDropdowns: true,
            startDate: formatDate(today),
        });

        $('#end_date').daterangepicker({
            singleDatePicker: true,
            showDropdowns: true,
            startDate: formatDate(sixDaysLater),
        });
    });
});

document.querySelectorAll('p.status').forEach(function (statusCell) {
    var statusText = statusCell.innerText.trim();

    if (statusText === '요청') {
        statusCell.classList.add('status-request');
    } else if (statusText === '승인') {
        statusCell.classList.add('status-approve');
    } else if (statusText === '반려') {
        statusCell.classList.add('status-reject');
    }
});

// 제목을 클릭하면 해당 글로 이동하는 기능
document.querySelectorAll('td.clickable').forEach(function (titleCell) {
    titleCell.addEventListener('click', function () {
        window.location.href = titleCell.getAttribute('data-href');
    });
});