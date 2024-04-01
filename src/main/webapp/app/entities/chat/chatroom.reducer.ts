import { createSlice } from '@reduxjs/toolkit';
import { Message } from 'app/shared/model/message.model';

interface ChatState {
  messages: Message[];
}

const initialState: ChatState = {
  messages: [],
};

const chatSlice = createSlice({
  name: 'chat',
  initialState,
  reducers: {
    messageSent(state: ChatState, action) {
      if (action.payload.recipient !== 'public') {
        state.messages.push(action.payload);
      }
    },
    messageReceived(state: ChatState, action) {
      state.messages.push(action.payload);
    },
  },
});

export const { messageSent, messageReceived } = chatSlice.actions;
export default chatSlice.reducer;
