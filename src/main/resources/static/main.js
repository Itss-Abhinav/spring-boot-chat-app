'use strict';

const usernamePage = document.querySelector('#username-page');
const chatPage = document.querySelector('#chat-page');
const usernameForm = document.querySelector('#usernameForm');
const messageForm = document.querySelector('#messageForm');
const messageInput = document.querySelector('#message');
const messageArea = document.querySelector('#messageArea');

let stompClient = null;
let username = null;

// Step 1: Connect when the user submits their username
function connect(event) {
    username = document.querySelector('#name').value.trim();

    if (username) {
        usernamePage.classList.add('d-none'); // Hide login screen
        chatPage.classList.remove('d-none'); // Show chat screen

        // Initialize SockJS and STOMP
        const socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    }
    event.preventDefault();
}

function onConnected() {
    // Subscribe to the Public Topic so we can receive messages
    stompClient.subscribe('/topic/public', onMessageReceived);

    // Tell the server your name
    stompClient.send("/app/chat.addUser",
        {},
        JSON.stringify({sender: username, type: 'JOIN'})
    );
}

function onError(error) {
    alert("Could not connect to WebSocket server. Please refresh and try again.");
}

// Step 2: Send a message to the server
function sendMessage(event) {
    const messageContent = messageInput.value.trim();

    if (messageContent && stompClient) {
        const chatMessage = {
            sender: username,
            content: messageContent,
            type: 'CHAT'
        };

        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = ''; // clear input field
    }
    event.preventDefault();
}

// Step 3: Display a message when received from the server
function onMessageReceived(payload) {
    const message = JSON.parse(payload.body);
    const messageElement = document.createElement('li');
    messageElement.classList.add('mb-2', 'p-2', 'rounded');

    if (message.type === 'JOIN') {
        messageElement.classList.add('text-center', 'text-muted');
        messageElement.innerText = message.content;
    } else if (message.type === 'LEAVE') {
        messageElement.classList.add('text-center', 'text-muted');
        messageElement.innerText = message.sender + ' left!';
    } else {
        messageElement.classList.add('bg-light', 'border');
        messageElement.innerHTML = `<strong>${message.sender}:</strong> ${message.content}`;
    }

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight; // Auto-scroll to bottom
}

// Event Listeners
usernameForm.addEventListener('submit', connect, true);
messageForm.addEventListener('submit', sendMessage, true);