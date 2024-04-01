import SockJS from 'sockjs-client';
import Stomp from 'webstomp-client';
import { Observable } from 'rxjs';
import { Storage } from 'react-jhipster';

import { getAccount, logoutSession } from 'app/shared/reducers/authentication';
import { messageReceived } from 'app/entities/chat/chatroom.reducer';
import { Message } from 'app/shared/model/message.model';

let stompClient = null;

let subscriber = null;
let connection: Promise<any>;
let connectedPromise: any = null;
let listener: Observable<any>;
let listenerObserver: any;
let alreadyConnectedOnce = false;

const createConnection = (): Promise<any> => new Promise(resolve => (connectedPromise = resolve));

const createListener = (): Observable<any> =>
  new Observable(observer => {
    listenerObserver = observer;
  });

export const sendMessage = (message: Message) => {
  connection?.then(() => {
    stompClient?.send(
      '/chat', // destination

      JSON.stringify(message), // body
      {}, // header
    );
  });
};

const subscribe = () => {
  connection.then(() => {
    subscriber = stompClient.subscribe('/chat/public', data => {
      listenerObserver.next(JSON.parse(data.body));
    });
  });
};

const connect = () => {
  if (connectedPromise !== null || alreadyConnectedOnce) {
    // the connection is already being established
    return;
  }
  connection = createConnection();
  listener = createListener();

  // building absolute path so that websocket doesn't fail when deploying with a context path
  const loc = window.location;
  const baseHref = document.querySelector('base').getAttribute('href').replace(/\/$/, '');

  const headers = {};
  let url = '//' + loc.host + baseHref + '/websocket/chat';
  const authToken = Storage.local.get('jhi-authenticationToken') || Storage.session.get('jhi-authenticationToken');
  if (authToken) {
    url += '?access_token=' + authToken;
  }
  const socket = new SockJS(url);
  stompClient = Stomp.over(socket, { protocols: ['v12.stomp'] });

  stompClient.connect(headers, () => {
    connectedPromise('success');
    connectedPromise = null;
    alreadyConnectedOnce = true;
  });
};

const disconnect = () => {
  if (stompClient !== null) {
    if (stompClient.connected) {
      stompClient.disconnect();
    }
    stompClient = null;
  }
  alreadyConnectedOnce = false;
};

const receive = () => listener;

const unsubscribe = () => {
  if (subscriber !== null) {
    subscriber.unsubscribe();
  }
  listener = createListener();
};

export default store => next => action => {
  if (getAccount.fulfilled.match(action)) {
    connect();
    const isUser = action.payload.data.authorities.includes('ROLE_USER');
    if (!alreadyConnectedOnce && isUser) {
      subscribe();
      receive().subscribe(message => {
        return store.dispatch(messageReceived(message));
      });
    }
  } else if (getAccount.rejected.match(action) || action.type === logoutSession().type) {
    unsubscribe();
    disconnect();
  }
  return next(action);
};
