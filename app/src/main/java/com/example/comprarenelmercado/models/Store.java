package com.example.comprarenelmercado.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Store implements Serializable {
    String idStore, nameStore;
    List<Product> products= new ArrayList<Product>();

    public Store() {
    }

    public Store(String idStore, String nameStore) {
        this.idStore = idStore;
        this.nameStore = nameStore;
    }

    public String getIdStore() {
        return idStore;
    }

    public String getNameStore() {
        return nameStore;
    }

    public void setNameStore(String nameStore) {
        this.nameStore = nameStore;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public  void editProducts(Product product){
        List<Product> editedProducts= new ArrayList<>();
        //The product list of the store is iterated
        for (int i = 0; i<products.size(); i++){
            Product myProduct= products.get(i);

            //If a product is the product being edited (same id) it is replaced by the new product
            if (myProduct.getIdProduct().equals(product.getIdProduct())){
                myProduct=product;
            }
            //The products are added to the new list
            editedProducts.add(myProduct);
        }
        //Finally the list is added to the store
        products.clear();
        products=editedProducts;
    }

    public void addProduct(Product pr){products.add(pr);}
}
