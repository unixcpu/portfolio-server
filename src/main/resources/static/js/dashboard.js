// 서버에서 작업 데이터 가져오기
fetch('/projects/api/dashboard')
    .then(response => response.json())
    .then(tasks => {
        // 작업 상태 계산
        const totalTasks = tasks.length;
        const completedTasks = tasks.filter(task => task.status === 'done').length;
        const incompleteTasks = tasks.filter(task => task.status !== 'done').length;

        const now = new Date();
        const overdueTasks = tasks.filter(task => new Date(task.dueDate) < now && task.status !== 'done').length;

        // 대시보드에 표시
        document.getElementById('total-tasks').textContent = totalTasks;
        document.getElementById('completed-tasks').textContent = completedTasks;
        document.getElementById('incomplete-tasks').textContent = incompleteTasks;
        document.getElementById('overdue-tasks').textContent = overdueTasks;
    })
    .catch(error => {
        console.error('Error fetching tasks:', error);
    });
