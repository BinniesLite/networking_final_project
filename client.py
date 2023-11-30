import socket
import threading

def receive_messages():
    while True:
        try:
            message = client.recv(1024).decode('utf-8')
            if message.startswith("exit"):
                break
                
            print(message)
        except Exception as e:
            print(f'An error occurred: {e}')
            break
        finally:
            return
            break

def send_messages():
    
    while True:
        try:
            command = input()
            client.send(command.encode('utf-8'))
        except Exception as e:
            print(f'Exiting the group chat {e}')
            break
        finally:
            return

if __name__ == "__main__":
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
    host, port = "localhost", 6789
    client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    client.connect((host, port))
    
    try: 
        # Start receiving messages in a separate thread
        receive_thread = threading.Thread(target=receive_messages)
        receive_thread.start()
        
        # Start sending messages in a separate thread
        send_thread = threading.Thread(target=send_messages)
        send_thread.start()

        # Join threads to ensure proper exit
        receive_thread.join()
        send_thread.join()
    except OSError:
        client.close() 
    finally:
        client.close()
