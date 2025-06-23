// ===== calendar.js: Jana Kalendar Dinamik =====

document.addEventListener("DOMContentLoaded", function () {
  const calendarContainer = document.querySelector(".calendar-widget");
  const calendarTitle = document.getElementById("calendar-title-display");
  const currentDate = new Date();
  let currentMonth = currentDate.getMonth();
  let currentYear = currentDate.getFullYear();

  updateCalendar();

  function updateCalendar() {
    generateCalendar(currentMonth, currentYear);
    calendarTitle.textContent = new Intl.DateTimeFormat('ms-MY', { 
        month: 'long', 
        year: 'numeric' 
    }).format(new Date(currentYear, currentMonth));
  }

  function generateCalendar(month, year) {
    // Buang semua kecuali header
    calendarContainer.innerHTML = "";
    
    const daysOfWeek = ["Sun", "Mon", "Tues", "Wed", "Thur", "Fri", "Sat"];
    daysOfWeek.forEach(day => {
      const header = document.createElement("div");
      header.className = "day-header";
      header.textContent = day;
      calendarContainer.appendChild(header);
    });

    const firstDay = new Date(year, month, 1).getDay();
    const daysInMonth = new Date(year, month + 1, 0).getDate();

    for (let i = 0; i < firstDay; i++) {
      const empty = document.createElement("div");
      calendarContainer.appendChild(empty);
    }

    for (let day = 1; day <= daysInMonth; day++) {
      const dayBox = document.createElement("div");
      dayBox.className = "day";
      dayBox.textContent = day;
      
      //Highlight hari ini
      const isToday =
              day === currentDate.getDate() &&
              month === currentDate.getMonth() &&
              year === currentDate.getFullYear();
      
      if(isToday){
          dayBox.style.backgroundColor = "#e2edff";
          dayBox.style.fontWeight = "bold";
      }
      
      calendarContainer.appendChild(dayBox);
    }
  }

  // Navigasi bulan
  document.getElementById("prevMonth").addEventListener("click", () => {
    currentMonth--;
    if (currentMonth < 0) {
      currentMonth = 11;
      currentYear--;
    }
    updateCalendar();
  });

  document.getElementById("nextMonth").addEventListener("click", () => {
    currentMonth++;
    if (currentMonth > 11) {
      currentMonth = 0;
      currentYear++;
    }
    updateCalendar();
  });
});
