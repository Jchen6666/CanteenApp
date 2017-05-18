package com.example.jerrychen.canteenapp;

import java.io.Serializable;

/**
 * Created by jerrychen on 3/31/17.
 */

public class Dish implements Serializable {
    private int id;
    private double alcohol,carbohydrates,energy,fat,price,protein,weight;
    private String description,pictureurl,title;


    public Dish() {

    }

    public Dish(int id, double alcohol, double carbohydrates, double energy, double fat, double price, double protein, double weight,
                String description, String pictureurl, String title )
    {
        this.id=id;
        this.alcohol=alcohol;
        this.carbohydrates=carbohydrates;
        this.energy=energy;
        this.fat=fat;
        this.price=price;
        this.protein=protein;
        this.weight=weight;
        this.description=description;
        this.pictureurl=pictureurl;
        this.title=title;

    }
    public void setId(int id){this.id=id;}

    public void  setTtitle(String title){this.title=title;}

    public int getId(){return id;}

    public double getAlcohol(){return alcohol;}

    public double getCarbohydrates(){return carbohydrates;}

    public double getEnergy(){return  energy;}

    public double getFat(){return fat;}

    public double getPrice(){return price;}

    public double getProtein(){return protein;}

    public double getWeight(){return weight;}

    public String getDescription(){return  description;}

    public String getTitle(){return title;}

    public String getPictureurl(){return pictureurl;}
}
