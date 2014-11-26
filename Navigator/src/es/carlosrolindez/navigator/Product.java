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
	
	public String orderPoint;
	public String handWorkCost;
	
	public String purchase;
	public String inProduction;
	public String inPlannedProduction;
	
	public String sale;
	public String usedInProduction;
	public String transfer;
	public String usedInPlannedProduction;
	
	
	
	
	Product (String reference, String description, String quantity, String stock, String cost, boolean[] boolArray,
			String purchase, String inProduction, String inPlannedProduction, String sale, String usedInProduction,
			String transfer, String usedInPlannedProduction)
	{
        this.reference = reference;
        this.description = description;
        this.quantity = quantity;
        this.stock = description;
        this.cost = cost;
        this.inBOM = boolArray[0];
        this.hasBOM = boolArray[1];   
        
        this.purchase = purchase;
        this.inProduction = inProduction;
        this.inPlannedProduction = inPlannedProduction;
    	
        this.sale = sale;
        this.usedInProduction = usedInProduction;
        this.transfer = transfer;
        this.usedInPlannedProduction = usedInPlannedProduction;
	}
	
	public Product() {
        this.reference = "";
        this.description = "";
        this.quantity = "";
        this.stock = "";
        this.cost = "";
        this.inBOM = false;
        this.hasBOM = false;        
        
        this.purchase = "";
        this.inProduction = "";
        this.inPlannedProduction = "";
    	
        this.sale = "";
        this.usedInProduction = "";
        this.transfer = "";
        this.usedInPlannedProduction = "";
	}

	@Override
    public int describeContents() {
        return 0;
    }
	
    @Override
    public void writeToParcel(Parcel parcel, int arg1) 
    {
    	boolean[] boolArray={inBOM, hasBOM};
    	
        parcel.writeString(reference);
        parcel.writeString(description);
        parcel.writeString(quantity);
        parcel.writeString(stock);
        parcel.writeString(cost);
        parcel.writeBooleanArray(boolArray);

        parcel.writeString(purchase);
        parcel.writeString(inProduction);
        parcel.writeString(inPlannedProduction);
 
        parcel.writeString(sale);
        parcel.writeString(usedInProduction);
        parcel.writeString(transfer);
        parcel.writeString(usedInPlannedProduction);
    }
     
    public static final Parcelable.Creator<Product> CREATOR = new Creator<Product>() 
    {
    	boolean[] boolArray;
        @Override
        public Product createFromParcel(Parcel parcel) {
            String reference = parcel.readString();
            String description = parcel.readString();
            String quantity = parcel.readString();
            String stock = parcel.readString();
            String cost = parcel.readString();
            parcel.readBooleanArray(boolArray);
   
            String purchase = parcel.readString();
            String inProduction = parcel.readString();
            String inPlannedProduction = parcel.readString();

            String sale = parcel.readString();
            String usedInProduction = parcel.readString();
            String transfer = parcel.readString();
            String usedInPlannedProduction = parcel.readString();
              
            return new Product(reference, description, quantity, stock, cost, boolArray,
            		purchase, inProduction, inPlannedProduction, sale, usedInProduction,
        			transfer, usedInPlannedProduction);
        }
 
        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
         
    };

}
