  function setClientTime() {
            var currentTime = new Date();
            var hours = currentTime.getHours();
            var minutes = currentTime.getMinutes();
            var seconds = currentTime.getSeconds();
            if (minutes < 10)
                minutes = '0' + minutes;
            if (seconds < 10)
                seconds = '0' + seconds;
            document.getElementById('ClientTime').innerHTML = hours + ':' + minutes + ':' + seconds;
        }
        function refresh() {
            location.reload();
        }
