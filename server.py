# CS4065 Final Project: server

import socket
import threading
import time

def broadcast(message):
    for client in clients:
        client.send(message)

def handle_client(client):
    while True:
        try:
            message = client.recv(1024).decode('utf-8')
            if message:
                command = message.split(' ')[0]
            
            if command == '%join':
                username = message.split(' ')[1]
                if username in usernames.values():
                    client.send('Username already exists. Choose another.\n'.encode('utf-8'))
                else:
                    usernames[client] = username
                    broadcast(f'{username} has joined the chat room!\n'.encode('utf-8'))
                    
                    for msg in messages[-2:]:
                        client.send(msg.encode('utf-8'))
                    
                    user_list = "\n".join(usernames.values())
                    client.send(f"Users in the group:\n{user_list}\n".encode('utf-8'))
            
            elif command == '%connect':
                address_port = message.split(' ')[1:]
                if len(address_port) == 2:
                    try:
                        new_host = address_port[0]
                        new_port = int(address_port[1])
                        client.close()
                        new_client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
                        new_client.connect((new_host, new_port))
                        client.send(f'Connected to specified address {new_host} and port {new_port}.\n'.encode('utf-8'))
                    except Exception as e:
                        client.send(f'Error connecting: {str(e)}'.encode('utf-8'))
                else:
                    client.send('Invalid command format. Use "%connect [address] [port]\n".'.encode('utf-8'))
            
            elif command == '%post':
                subject = " ".join(message.split(' ')[1:])
                sender = usernames[client]
                
                # Get current timestamp
                post_date = time.strftime('%Y-%m-%d %H:%M:%S')
        
                # Message format updated to include timestamp
                full_message = f"Message ID: {len(messages) + 1}, Sender: {sender}, Post Date: {post_date}, Subject: {subject}\n"
                messages.append(full_message)
                broadcast(full_message.encode('utf-8'))
                
            elif command == '%users':
                user_list = "\n".join(usernames.values())
                client.send(f"Users in the group:\n{user_list}\n".encode('utf-8'))
                
            elif command == '%leave':
                username = usernames[client]
                del usernames[client]
                clients.remove(client)
                broadcast(f'{username} has left the chat room!\n'.encode('utf-8'))
                client.send("You left the chat room!\n".encode('utf-8'))

            elif command == '%message':
                msg_id = int(message.split(' ')[1])
                if 1 <= msg_id <= len(messages):
                    client.send(messages[msg_id - 1].encode('utf-8'))
                else:
                    client.send("Invalid message ID.\n".encode('utf-8'))
                
            elif command == '%exit':
                username = usernames[client]
                del usernames[client]
                clients.remove(client)
                broadcast(f'{username} has left the chat room!\n'.encode('utf-8'))
                client.send("You are disconnected from the server!\n".encode('utf-8'))
                client.close()
                break
                
            else:
                client.send('Invalid command format. Try again!\n'.encode('utf-8'))
                
        except:
            break

def run_server():
    print("Server started. Waiting for connections...")
    while True:
        client, address = server.accept()
        clients.append(client)
        print(f'Connection established with {address}')
        client.send("""Welcome to the chat room!
        Available commands:
        %connect [address] [port]: Connect to a different server
        %join [username]: Join the group
        %post [subject] [content]: Post a message
        %users: Get the list of users in the group
        %message [message ID]: Retrieve the content of a message
        %leave: Leave the group
        %exit: Exit the client program
        ---------------------------------------------------------""".encode('utf-8'))

        thread = threading.Thread(target=handle_client, args=(client,))
        thread.start()

if __name__ == "__main__":
    
    host = 'localhost'
    port = 6789
    server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server.bind((host, port))
    server.listen()
    
    clients = []
    usernames = {}
    messages = []
    
    run_server()
