#!/usr/bin/zsh

for index in {1..5}; do
    data='{
             "productName": "Chocolate",
             "productPrice": 100.0,
             "productBarcode": "XXX-123",
             "productDescription": "Made of Cocoa from West Africa"
          }'

    postResults=$(curl -X POST -H "Content-Type: application/json" -d "$data" "http://localhost:8080/product")
    echo "Saved product $index-ID: $postResults"
    productId=$(echo "$postResults" | cut -d '/' -f 5)

    curl "http://localhost:8080/product/$productId" | jq

    details='{
                "productName": "Chocolate",
                "quantity": 5
            }'

    curl -X POST -H "Content-Type: application/json" -d "$details" "http://localhost:8080/product/inventory/increment/$productId" | jq
done