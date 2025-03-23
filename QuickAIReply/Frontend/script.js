document.getElementById("generateBtn").addEventListener("click", function() {
    let userInput = document.getElementById("userInput").value.trim();
    let selectedTone = document.getElementById("toneSelect").value;

    if (userInput === "") {
        alert("Please enter a message.");
        return;
    }

    fetch("http://localhost:8080/api/Mail/process", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ content: userInput, tone: selectedTone })
    })
    .then(response => response.text())
    .then(data => {
        document.getElementById("outputContainer").style.display = "block";
        document.getElementById("outputText").innerText = data;
    })
    .catch(error => {
        console.error("Error:", error);
        document.getElementById("outputText").innerText = "Error generating email.";
    });
});
