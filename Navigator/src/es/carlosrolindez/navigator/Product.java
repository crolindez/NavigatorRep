package es.carlosrolindez.navigator;

import android.os.Parcel;
import android.os.Parcelable;


public class Product implements Parcelable{
	public Boolean inBOM;
	public String reference;
	public String description;
	public String quantity;
	public String stock;
	public String cost;
	public Boolean hasBOM;
	
	
	Product (String reference, String description, String quantity, String stock, String cost, boolean[] boolArray){
	        this.reference = reference;
	        this.description = description;
	        this.quantity = quantity;
	        this.stock = description;
	        this.cost = cost;
	        this.inBOM = boolArray[0];
	        this.hasBOM = boolArray[1];        
	}
	
	public Product() {
        this.reference = "";
        this.description = "";
        this.quantity = "";
        this.stock = "";
        this.cost = "";
        this.inBOM = false;
        this.hasBOM = false;        
	}

	@Override
    public int describeContents() {
        return 0;
    }
	
    @Override
    public void writeToParcel(Parcel parcel, int arg1) {
    	boolean[] boolArray={inBOM, hasBOM};
    	
        parcel.writeString(reference);
        parcel.writeString(description);
        parcel.writeString(quantity);
        parcel.writeString(stock);
        parcel.writeString(cost);
        parcel.writeBooleanArray(boolArray);
    }
     
    public static final Parcelable.Creator<Product> CREATOR = new Creator<Product>() {
    	boolean[] boolArray;
        @Override
        public Product createFromParcel(Parcel parcel) {
            String reference = parcel.readString();
            String description = parcel.readString();
            String quantity = parcel.readString();
            String stock = parcel.readString();
            String cost = parcel.readString();
            parcel.readBooleanArray(boolArray);
            
            return new Product(reference, description, quantity, stock, cost, boolArray);
        }
 
        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
         
    };

}
