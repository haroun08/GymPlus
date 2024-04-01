import React, { useState } from 'react';
import { useDispatch } from 'react-redux';
import { useAppSelector } from 'app/config/store';
import { sendMessage } from 'app/config/chat-middleware';
import { Message } from 'app/shared/model/message.model';
import { messageSent } from './chatroom.reducer';

import './Chatroom.scss';

function Chatroom() {
  const [message, setMessage] = useState('');
  const dispatch = useDispatch();
  const messages = useAppSelector(state => state.chatroomReducer.messages);
  const account = useAppSelector(state => state.authentication.account);

  const handleSendMessage = (event: React.FormEvent) => {
    event.preventDefault();
    if (message.trim() !== '') {
      const messageToSend: Message = {
        sender: account.login,
        recipient: 'public',
        message,
        time: new Date().toISOString(),
      };
      sendMessage(messageToSend);
      dispatch(messageSent(messageToSend));
      setMessage('');
    }
  };

  return (
    <div className="chatroom-container">
      <h1 className="chatroom-header">Chatroom</h1>
      <div className="messages-container">
        {messages.map((msg, index) => (
          <div key={index} className={`message-bubble ${msg.sender === 'Me' ? 'sent-message' : 'received-message'}`}>
            <span className="message-text">{msg.message}</span>
          </div>
        ))}
      </div>
      <form onSubmit={handleSendMessage} className="message-input-container">
        <input
          type="text"
          value={message}
          onChange={event => setMessage(event.target.value)}
          placeholder="Type a message"
          className="message-input"
        />
        <button type="submit" className="send-button">
          Send
        </button>
      </form>
    </div>
  );
}

export default Chatroom;
