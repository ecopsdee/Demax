
package dee.models;

import dee.database.deeinventory;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


public class Stock {
    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleStringProperty table;
    private SimpleIntegerProperty qty;
    private SimpleIntegerProperty cart;
    private SimpleIntegerProperty piece;
    private SimpleStringProperty date;
    private SimpleIntegerProperty invoice;
    private SimpleIntegerProperty remaingoods;
    private SimpleIntegerProperty nop;
    private SimpleIntegerProperty nogpp;
    private SimpleIntegerProperty tnog;
    private SimpleIntegerProperty nocr;
    private SimpleIntegerProperty pog;
    private SimpleIntegerProperty tnogr;
    
    private Integer stockID;
    private String stockTABLE;
    private Integer numberOfpackets;
    private Integer numberOfgoodsPerPacket;
    private Integer numberOfcartonsRemaining;
    private Integer totalnumberOfgoodsRemaining;
    private Integer PiecesOfGoods;
    private deeinventory DbSet;

    public int getId() {
        return id.get();
    }

    public String getName() {
        return name.get();
    }

    public String getTable() {
        return table.get();
    }

    public int getQty() {
        return qty.get();
    }

    public int getCart() {
        return cart.get();
    }

    public int getPiece() {
        return piece.get();
    }

    public Integer getStockID() {
        return stockID;
    }

    public String getStockTABLE() {
        return stockTABLE;
    }

    public Integer getNumberOfcartonsRemaining() {
        return numberOfcartonsRemaining;
    }

    public Integer getTotalnumberOfgoodsRemaining() {
        return totalnumberOfgoodsRemaining;
    }

    public Integer getPiecesOfGoods() {
        return PiecesOfGoods;
    }

    public Integer getNumberOfpackets() {
        return numberOfpackets;
    }

    public Integer getNumberOfgoodsPerPacket() {
        return numberOfgoodsPerPacket;
    }

    public String getDate() {
        return date.get();
    }

    public int getInvoice() {
        return invoice.get();
    }

    public int getRemaingoods() {
        return remaingoods.get();
    }

    public int getNop() {
        return nop.get();
    }

    public int getNogpp() {
        return nogpp.get();
    }

    public int getTnog() {
        return tnog.get();
    }

    public int getNocr() {
        return nocr.get();
    }

    public int getPog() {
        return pog.get();
    }

    public int getTnogr() {
        return tnogr.get();
    }

    
    
    
    
    public Stock(){
    }
    
    public Stock(Integer id, String name, String table){
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.table = new SimpleStringProperty(table);   
    }
    
    public Stock(String name, Integer qty, Integer cart, Integer pieces){
        this.name = new SimpleStringProperty(name);
        this.qty = new SimpleIntegerProperty(qty);
        this.cart = new SimpleIntegerProperty(cart);
        this.piece = new SimpleIntegerProperty(pieces);
    }
    
    public Stock(Integer STID, String STABLE){
        this.stockID = STID;
        this.stockTABLE = STABLE;
    }
    
    public Stock(Integer NSOCR, Integer TSNOGR, Integer POG, Integer NOP, Integer NOGPP ){
        this.numberOfcartonsRemaining = NSOCR;
        this.totalnumberOfgoodsRemaining = TSNOGR;
        this.PiecesOfGoods = POG;
        this.numberOfpackets = NOP;
        this.numberOfgoodsPerPacket = NOGPP;
    }

    public Stock(Integer cart, String date, Integer invoice, Integer remaingoods, Integer NOP, Integer NOGPP, Integer TNOG, Integer NOCR, Integer POG, Integer TNOGR) {
        this.cart = new SimpleIntegerProperty(cart);
        this.date = new SimpleStringProperty(date);
        this.invoice = new SimpleIntegerProperty(invoice);
        this.remaingoods = new SimpleIntegerProperty(remaingoods);
        this.nop = new SimpleIntegerProperty(NOP);
        this.nogpp = new SimpleIntegerProperty(NOGPP);
        this.tnog = new SimpleIntegerProperty(TNOG);
        this.nocr = new SimpleIntegerProperty(NOCR);
        this.pog = new SimpleIntegerProperty(POG);
        this.tnogr = new SimpleIntegerProperty(TNOGR);
    }
    
    
    
    
}
