// 드래그 시작 시
function dragStart(e) {
    e.dataTransfer.setData('text/plain', e.target.id);
    e.target.classList.add('dragging');
}

// 드래그 오버 시
function allowDrop(e) {
    e.preventDefault();
}

// 드롭 시
function dropTask(e) {
    e.preventDefault();
    const taskIdWithPrefix = e.dataTransfer.getData('text'); // 드래그된 작업 ID 가져오기
    const taskId = taskIdWithPrefix.replace('task-', ''); // "task-" 접두사 제거하여 숫자 ID로 변환
    const task = document.getElementById(taskIdWithPrefix); // 작업 요소 가져오기

    // 드롭된 요소의 부모가 아닌 경우에만 append
    if (!e.target.classList.contains('task')) {
        e.target.appendChild(task); // 작업을 드롭한 위치에 추가
    }

    // 드롭된 요소의 ID를 통해 새로운 상태를 결정
    const newStatus = e.target.id; // 드롭된 요소의 ID를 상태로 사용

    // 작업 상태 업데이트 및 서버 전송
    updateTaskStatus(taskId, newStatus); // 상태를 서버에 전송
}


// 작업 상태를 서버에 전송하는 함수
function updateTaskStatus(taskId, newStatus) {
    // AJAX 요청
    fetch(`/projects/dragdrop`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ id: taskId, status: newStatus }) // ID와 새로운 상태를 JSON 형태로 전송
    })
    .then(response => {
        if (response.ok) {
            console.log('작업 상태가 성공적으로 업데이트되었습니다.');
        } else {
            console.error('업데이트 실패:', response.statusText);
        }
    })
    .catch(error => {
        console.error('서버와의 통신 오류:', error);
    });
}

// 칸반 컬럼들에 드래그 이벤트 추가
const columns = document.querySelectorAll('.task-list');
columns.forEach((column) => {
    column.addEventListener('dragover', allowDrop);
    column.addEventListener('drop', dropTask);
});

// 페이지 로딩 후 작업 상태 저장 함수
function saveTasksOnLoad() {
    const tasks = document.querySelectorAll('.task'); // 모든 작업 요소 선택
    tasks.forEach(task => {
        const taskId = task.getAttribute('data-id'); // 작업 ID
        const taskName = task.textContent; // 작업 이름
        const status = task.parentElement.id; // 부모 요소의 ID를 사용하여 상태 확인
    });
}

// 페이지가 완전히 로딩된 후 saveTasksOnLoad 함수 호출
window.onload = function() {
    saveTasksOnLoad();
};
