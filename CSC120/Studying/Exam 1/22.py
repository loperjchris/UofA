tweets = [("user1", "I <3 OOP! #csc120"), ("user2", "@user1 me too! #csc120"),\
    ("user3", "Can someone help me with my #csc110 homework?"),\
    ("user1", "Go to the 228 lab for help in #csc120")]

def parse_tweets(tweets):
    d = {}
    for line in tweets:
        user = line[0]
        tweet = line[1]
        temp = tweet.split(" ")
        for element in temp:
            if element[0] == "#":
                if element not in d:
                    d[element] = {"tweets":{}}
                if user not in d[element]["tweets"]:
                    d[element]["tweets"][user] = [tweet]
                else:
                    d[element]["tweets"][user].append(tweet)
    print (d)
    s = "{\n        "
    for hashtag in d:
        s += hashtag + ": {\n   'tweets': { "
        for users in d[hashtag]["tweets"]:
            s += users + ":" + str(d[hashtag]["tweets"][users] + ",\n")
        s += "}"
    print (s)



parse_tweets(tweets)
