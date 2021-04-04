import json

data_file = open('options.json').read()
options = json.loads(data_file)

def printQue(type):
    i = 1
    for question in options['options'][type-1]['questions']:
        print(str(i)+'. '+str(question))
        i+=1
    return False

def printResp(type, que):
    print(options['options'][type-1]['responses'][que-1])
    return False

flag = True
while(flag):
    type = int(input('Is this regarding:\n\t1. Product/Order\n\t2. App\n(input 0 to exit)'))
    if type == 1 or 2:
        flag = printQue(type)
        flag1 = True
        while(flag1):
            que = int(input('Which option best describes your issue? (input 0 to exit)'))
            if que > 1 and que <= len(options['options'][type-1]['questions']):
                flag1 = printResp(type, que)
            elif que == 0:
                flag1 = False
            else:
                print('Invalid input')
    elif type == 0:
        flag = False
    else:
        print('Invalid input.')
