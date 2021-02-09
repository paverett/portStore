from pymongo import MongoClient
import random

def main():
    client = MongoClient(port=27017)
    db=client.prices
    item_list = ["13860428", "54456119", "13264003", "12954218"]
    for item in item_list:
        db.priceCollection.insert_one({"id": item, "price": {
            "value": round(random.uniform(3, 15), 2),
            "currencyCode": "USD"
        }})

if __name__ == "__main__":
    main()