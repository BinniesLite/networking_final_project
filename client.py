import socket
import threading

def receive_messages():
    while True:
        try:
            message = client.recv(1024).decode('utf-8')
            print(message)
        except:
            print('An error occurred!')
            client.close()
            break

def send_messages():
    while True:
        try:
            command = input()
            
            # Send commands to the server
            client.send(command.encode('utf-8'))
            
        except:
            print('An error occurred!')
            client.close()
            break

if __name__ == "__main__":
    host = 'localhost'
    port = 6789
    client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    client.connect((host, port))
    
    # Start receiving messages in a separate thread
    receive_thread = threading.Thread(target=receive_messages)
    receive_thread.start()
    
    # Start sending messages (commands) in the main thread
    send_thread = threading.Thread(target=send_messages)
    send_thread.start()
