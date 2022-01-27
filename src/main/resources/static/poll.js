let poll = document.getElementById("poll");
let pollResults = document.getElementById("pollResults");
let pollData = document.getElementById("pollData");

window.addEventListener("DOMContentLoaded", () => {
    let result = JSON.parse(pollData.innerText);
    if (didVote()) {
        poll.style.display = "none";
        pollResults.style.display = "block";
        addPoll(result);
    } else {
        poll.style.display = "block";
        pollResults.style.display = "none";

    }
})

let didVote = () => {
    return getCookie().split("voted_on_1=")[1].split(";")[0] == "true";
}

function getCookie() {
    return document.cookie;
}

let addPoll = (jsonResult) => {
    pollResults.innerHTML = `
        <p>${jsonResult.pollQuestion}</p>
    `
    let xValues = [];
    let yValues = [];
    let colors = [];
    let sumOfResults = 0;
    for (let res of jsonResult.results) {
        sumOfResults += parseInt(res["votes_number"]);
    }
    jsonResult.results.sort( function(a,b) {
        let k1 = a.votes_number;
        let k2 = b.votes_number;
        if (k1 > k2) return -1;
        if (k1 < k2) return 1;
        return 0;
    })

    for (let res of jsonResult.results) {
        xValues.push(res.option_name.substring(0, 4)+"..");
        yValues.push(res.votes_number);
        colors.push("brown");
        pollResults.innerHTML += `
            <div class="bar">
                <div class="bar-filled" style="width: calc(${res["votes_number"]}/${sumOfResults} * 100%);"></div>
            </div>
            <p>${res["option_name"]}: ${res["votes_number"]}</p>
        `
    }
}
