document.addEventListener("DOMContentLoaded", function () {
    const calendarGrid = document.getElementById("calendarGrid");
    const calendarTitle = document.getElementById("calendar-title");
    const form = document.querySelector(".booking-form");
    const popup = document.getElementById("bookingPopup");
    const overlay = document.getElementById("overlay");

    let currentDate = new Date();

    function loadCalendar() {
        const year = currentDate.getFullYear();
        const month = currentDate.getMonth();

        const firstDay = new Date(year, month, 1);
        const lastDay = new Date(year, month + 1, 0);

        calendarTitle.textContent = firstDay.toLocaleString('default', {
            month: 'long',
            year: 'numeric'
        });

        calendarGrid.innerHTML = "";

        const startDay = firstDay.getDay();
        for (let i = 0; i < startDay; i++) {
            const empty = document.createElement("div");
            empty.classList.add("day", "empty");
            calendarGrid.appendChild(empty);
        }

        for (let day = 1; day <= lastDay.getDate(); day++) {
            const dateStr = `${year}-${String(month + 1).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
            const dayEl = document.createElement("div");
            dayEl.classList.add("day");
            dayEl.textContent = day;

            fetch(`CheckDateAvailabilityServlet?homestay_id=${HOMESTAY_ID}&date=${dateStr}`)
                .then(res => res.json())
                .then(data => {
                    if (data.available) {
                        dayEl.classList.add("available");
                        dayEl.title = "Available";
                    } else {
                        dayEl.classList.add("booked");
                        dayEl.title = "Already Booked";
                    }
                });

            calendarGrid.appendChild(dayEl);
        }
    }

    // Navigasi bulan
    document.getElementById("prevMonth").addEventListener("click", () => {
        currentDate.setMonth(currentDate.getMonth() - 1);
        loadCalendar();
    });

    document.getElementById("nextMonth").addEventListener("click", () => {
        currentDate.setMonth(currentDate.getMonth() + 1);
        loadCalendar();
    });

    loadCalendar(); // paparan awal

    // Fungsi pop-up pengesahan
    if (form) {
        form.addEventListener("submit", function (e) {
            e.preventDefault();

            const homestayName = document.querySelector(".info-box h2").textContent;
            const checkIn = document.getElementById("checkIn").value;
            const checkOut = document.getElementById("checkOut").value;
            const guests = document.getElementById("totalGuests").value;
            const pricePerNight = parseFloat(document.querySelector(".info-box").dataset.price || "0");


            const days = Math.ceil(
                (new Date(checkOut) - new Date(checkIn)) / (1000 * 60 * 60 * 24)
            );
            const total = days * pricePerNight;

            document.getElementById("popupHomestay").textContent = homestayName;
            document.getElementById("popupCheckIn").textContent = checkIn;
            document.getElementById("popupCheckOut").textContent = checkOut;
            document.getElementById("popupGuests").textContent = guests;
            document.getElementById("popupTotal").textContent = `RM ${total.toFixed(2)}`;

            overlay.style.display = "block";
            popup.style.display = "block";
        });

        // Batal tempahan
        document.getElementById("cancelBooking").addEventListener("click", function () {
            overlay.style.display = "none";
            popup.style.display = "none";
        });

        // Sahkan tempahan
        document.getElementById("confirmBooking").addEventListener("click", function () {
            form.submit();
        });
    }
});
