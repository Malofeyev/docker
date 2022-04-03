let res = [
    db.Stocks.drop(),
    db.Stocks.createIndex({ companyName: 1 }, { unique: true }),
    db.Stocks.insert({ companyName: 'ya', count: 1000, price: 1}),
    db.Stocks.insert({ companyName: 'google', count: 1000, price: 1}),
    db.Stocks.insert({ companyName: 'microsoft', count: 1000, price: 1}),
]

printjson(res)
