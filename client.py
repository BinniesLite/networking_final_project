import socket
import threading

# client receive messages function
def receive_messages():
    while True:
        try:
            message = client.recv(1024).decode('utf-8')
            print(message)
        except Exception as e:
            print(f'An error occurred in client receive: {e}')
            break

# client send messages function
def send_messages():
    while True:
        try:
            command = input()
            client.send(command.encode('utf-8'))
        except Exception as e:
            print(f'Exiting the group chat: {e}')
            break

if __name__ == "__main__":
    host = 'localhost'
    port = 6789
    client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    client.connect((host, port))
    
    # Start receiving messages in a separate thread
    receive_thread = threading.Thread(target=receive_messages)
    receive_thread.start()
    
    # Start sending messages in a separate thread
    send_thread = threading.Thread(target=send_messages)
    send_thread.start()

    # Join threads to ensure proper exit
    receive_thread.join()
    send_thread.join()

    client.close()
