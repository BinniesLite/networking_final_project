import socket
import threading

def receive_messages():
    global running
    while running:
        try:
            message = client.recv(1024).decode('utf-8')
            if message.startswith("exit"):
                exit()
                print("Server has disconnected.")
                running = False
            else:
                print(message)
        except Exception as e:
            print(f'An error occurred: {e}')
            running = False
            break

def send_messages():
    global running
    
    while running:
        try:
            command = input()
            client.send(command.encode('utf-8'))
        except Exception as e:
            print(f'Exiting the group chat {e}')
            break

if __name__ == "__main__":
    running = True
    s = ""
    while True:
        s = input("Use command '%connect [address] [port]' to connect to the server\n")
        
        if not s.startswith("%connect"):
            print("invalid inputs")
            continue
        else:
            break 
    
    s = s.split(" ")
    host, port = s[1], int(s[2])
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