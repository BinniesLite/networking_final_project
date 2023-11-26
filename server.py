import socket
import threading
import time

def broadcast(message):
    for client in clients:
        client.send(message)

def broadcast_group(message, group_id):
    for client in group_users[group_id]:
        client.send(message)
    
def handle_client(client):
    while True:
        try:
            message = client.recv(1024).decode('utf-8')
            
            if message:
                command = message.split(' ')[0]
            
                temp = []
           
                # Handle empty string
                message = message.split(' ')
                for msg in message[1:]:
                    if msg != "":
                        temp.append(msg)

                message = [command] + temp
            # response = ""
            if command == '%join':
                username = message[1]
                print(username)
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
                address_port = message[1:]
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
                subject = message[1]
                content = " ".join(message[2:])
                
                sender = usernames[client]
                
                # Get current timestamp
                post_date = time.strftime('%Y-%m-%d %H:%M:%S')
        
                # Message format updated to include timestamp
                full_message = f"Message ID: {len(messages) + 1}, Sender: {sender}, Post Date: {post_date}, Subject: {subject}, Content:{content}\n"
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
                msg_id = int(message[1])
                
                if 1 <= msg_id <= len(messages):    
                    client.send(messages[msg_id - 1].encode('utf-8'))
                else:
                    client.send("Invalid message ID.\n".encode('utf-8'))
                
            elif command == '%exit':
                if username in usernames:
                    username = usernames[client]
                
                    client.send(f'{username} has left the chat room!\n'.encode('utf-8'))
                    del usernames[client]
                
                client.send("You are disconnected from the server!\n".encode('utf-8'))
                
                client.close()
                break
            elif command == "%groups":
                response = f"Here're all the group available: \n {' '.join([str(i) for i in range(GROUPS)])}" 
                
                client.send(response.encode("utf-8"))
            elif command == "%groupjoin":
                if len(message) < 2:
                    client.send('Invalid command format. Try again!\n'.encode('utf-8'))
                    continue
                group_id = int(message[1])
                
                if not (0 <= group_id <= GROUPS): 
                    client.send("Invalid group number".encode("utf-8"))
                    continue 
                
                if client in group_users[group_id]:
                    client.send(f"Users with id {group_id} exist".encode("utf-8"))
                    continue
                
                group_users[group_id].append(client)
                
                username = "Jane"
                if client in usernames:
                    username = usernames[client]
                
                
                client.send(f"User {username} join group {group_id}".encode("utf-8"))
            elif command == "%groupusers":
                if len(message) < 2:
                    client.send('Invalid command format. Try again!\n'.encode('utf-8'))
                    continue
                group_id = int(message[1]) 
                response = []
                
                if not (0 <= group_id <= GROUPS): 
                    client.send("Invalid group number".encode("utf-8"))
                    continue 
                
                for user in group_users[group_id]:
                    response.append(usernames[user])
                content = "\n".join([res for res in response])
                client.send(f"The user in {group_id} are: {content}".encode("utf-8"))
            elif command == "%grouppost":

                if len(message) <= 2:
                    client.send('Invalid command format. Try again!\n'.encode('utf-8'))
                    continue  
                
                
                group_id = int(message[1]) 
                subject = message[2]
                content = " ".join(message[3:])
                
                if client not in group_users[group_id]:
                    client.send("You don't have permission to post in this group".encode("utf-8"))
                    continue
                if not (0 <= group_id <= GROUPS): 
                    client.send("Invalid group number".encode("utf-8"))
                    continue 
                sender = ""
                if client in usernames:
                    sender = usernames[client]
                # Get current timestamp
                post_date = time.strftime('%Y-%m-%d %H:%M:%S')
                
                # Message format updated to include timestamp
                full_message = f"Message ID: {len(group_messages[group_id]) + 1}, Sender: {sender}, Post Date: {post_date}, Subject: {subject}, Content:{content}\n"
                
                group_messages[group_id] += [full_message]
                
                broadcast_group(full_message.encode('utf-8'), group_id)
            elif command == "%groupleave":
                group_id = int(message[1])
                if client not in group_users[group_id]:
                    client.send("You don't have permission to post in this group".encode("utf-8"))
                    continue
                
                client.send(f"{usernames[client]} leave group {group_id}".encode("utf-8"))
                
                group_users[group_id].remove(client)
                print(group_users[group_id])
            elif command == "%groupmessage":
                if len(message) <= 2:
                    client.send('Invalid command format. Try again!\n'.encode('utf-8'))
                    continue  
                
                group_id = int(message[1])
                msg_id = int(message[2])
                

                if client not in group_users[group_id]:
                    client.send(f"User is not allowed".encode('utf-8'))
                    continue
                
                if 1 <= msg_id <= len(group_messages[group_id]):    
                    client.send(group_messages[group_id][msg_id - 1].encode('utf-8'))
                else:
                    client.send("Invalid message ID.\n".encode('utf-8'))
                
            else:
                client.send('Invalid command format. Try again!\n'.encode('utf-8'))
            
            # client.send(response)
        except Exception as e:
            print(e)
            print("There is something wrong")
            continue
        
def run_server():
    print("Server started. Waiting for connections...")
    while True:
        client, address = server.accept()        
        clients.append(client)
        print(f'Connection established with {address}')
        client.send("""Welcome to the chat room!
        Available commands:
        %connect [address] [port]: Connect to a different server
        %join [username]: Join with the username
        %post [subject] [content]: Post a message
        %users: Get the list of users in the group
        %message [message ID]: Retrieve the content of a message
        %leave: Leave the group
        %exit: Exit the client program
        ---------------------------------------------------------
        Available group commands:
        %groups: Show all groups availables
        %groupsjoin [group ID]: Join the group with id
        %grouppost [group ID] [subject] [content]: Post to the group with that id
        %groupusers [group ID]: Retrieve a list of users in the given group
        %groupmessage [group ID] [message ID]: retrieve the content of an earlier post if you belong to that group
        %groupleave [group ID]: Leave the current group if client is in that group
        """.encode('utf-8'))

        thread = threading.Thread(target=handle_client, args=(client,))
        thread.start()

if __name__ == "__main__":    
    host = 'localhost'
    port = 6789
    GROUPS = 5
    
    server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server.bind((host, port))
    server.listen()
    
    
    clients: list[type(socket)] = []
    usernames = {} # client -> username
    messages = [] # messages
    group_messages = {} # group -> messages
    group_users = {} # group -> client
    
    for i in range(GROUPS): 
        group_users[i] = []
        group_messages[i] = []
    
    run_server()
