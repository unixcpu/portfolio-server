// 태스크 추가 함수 (AJAX 사용)
function addTask(taskName) {
    const projectId = document.getElementById('projectId').value;

    $.ajax({
        url: '/projects/' + projectId + '/tasks',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({ name: taskName }), // 태스크 이름을 JSON 형식으로 전송
        success: function(response) {
            // 응답으로 받은 새 태스크 정보로 HTML 추가
            const newRow = `
                <tr id="task-${response.id}" draggable="true" ondragstart="drag(event)">
                    <td>${response.name}</td>
                    <td>
                        <form th:action="@{/projects/${projectId}/tasks/${response.id}/status}" method="post" class="status-form">
                            <select name="status" onchange="submitStatusChange(this, ${response.id})">
                                <option value="TO_DO" ${response.status === 'TO_DO' ? 'selected' : ''}>할 일</option>
                                <option value="IN_PROGRESS" ${response.status === 'IN_PROGRESS' ? 'selected' : ''}>진행 중</option>
                                <option value="DONE" ${response.status === 'DONE' ? 'selected' : ''}>완료</option>
                            </select>
                        </form>
                    </td>
                </tr>
            `;
            $('#taskList').append(newRow); // tbody에 새 행 추가
        },
        error: function(xhr, status, error) {
            console.error('태스크 추가 실패:', error);
            alert('태스크 추가 실패: ' + xhr.responseText);
        }
    });
}

// Sortable.js를 사용하여 드래그 앤 드롭 기능 추가
const taskList = document.getElementById('taskList');
const sortable = new Sortable(taskList, {
    animation: 150,  // 드래그 애니메이션 효과
    onEnd: function (/**Event*/evt) {
        // 항목이 이동된 후 호출되는 함수 (우선순위 업데이트 등)
        updateTaskPriorities();
        submitTaskList();
    }
});

function updateTaskPriorities() {
    const rows = document.querySelectorAll('#taskList tr');
    rows.forEach((row, index) => {
        const priorityCell = row.querySelector('td:nth-child(1)');
        priorityCell.textContent = index + 1; // 우선순위를 업데이트 (1부터 시작)
    });
}

function submitTaskList() {
    const rows = document.querySelectorAll('#taskList tr');
    const tasks = [];

    rows.forEach(row => {
        const id = row.querySelector('td:nth-child(2)').textContent.trim(); // 태스크 ID 가져오기
        const priority = row.querySelector('td:nth-child(1)').textContent.trim(); // 우선순위 값 가져오기
        const status = row.querySelector('select[name="status"]').value; // 상태 값 가져오기

        // ID가 숫자로 변환 가능한지 확인
        const parsedId = parseInt(id);
        if (isNaN(parsedId)) {
            console.error(`Invalid ID extracted from row: ${row.id}`); // ID가 유효하지 않음을 콘솔에 출력
        } else {
            // 각 태스크 정보를 객체로 저장
            tasks.push({ id: parsedId, priority: parseInt(priority), status: status });
        }
    });

    // AJAX 요청
    fetch(`/projects/tasklist`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(tasks) // 태스크 리스트를 JSON 형태로 변환
    })
    .then(response => {
        if (response.ok) {
            alert('태스크가 성공적으로 업데이트되었습니다.');
        } else {
            alert('업데이트 실패. 다시 시도해주세요.');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('오류가 발생했습니다. 다시 시도해주세요.');
    });
}