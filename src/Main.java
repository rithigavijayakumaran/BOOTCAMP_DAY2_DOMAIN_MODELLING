import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        }

}

class SwiggyPlatform{
    List<User> users = new ArrayList<>();
    List<Restaurant> restaurants = new ArrayList<>();
    List<DeliveryPerson> deliveryPeople = new ArrayList<>();
}
class User{
    private String email;
    private String contactNumber;
    private List<Address> addressList = new ArrayList<>();

    public User(String email, String contactNumber) {
        this.email = email;
        this.contactNumber = contactNumber;

    }
    public void addAddress(Address address){
        addressList.add(address);
    }

    public List<Address> getAddressList() {
        return addressList;
    }
}

class Address{
    private String doorNumber;
    private String street;
    private String city;
    private String state;
    private String zipcode;

    public Address(String doorNumber, String street, String city, String state, String zipcode) {
        this.doorNumber = doorNumber;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
    }
}

class Customer extends  User{
    public Customer(String email, String contactNumber) {
        super(email, contactNumber);
    }
    List<Order> pastOrders = new ArrayList<>();
    public void placeOrder(RestaurantBranch branch,int id,Map<FoodItem, Integer> items){
      Order order = new Order(id,LocalDateTime.now(),branch);
      for (Map.Entry<FoodItem, Integer> entry : items.entrySet()) {
          order.addItemToOrderList(entry.getKey(), entry.getValue());
      }
        order.calculateOrderTotal();
        pastOrders.add(order);
  }
  public   void cancelOrder(Order order){
      order.setOrderStatus(OrderStatus.CANCELLED);
      if (order.getPaymentStatus() == PaymentStatus.COMPLETED) {
          order.setPaymentStatus(PaymentStatus.REFUNDED);
      }

  }
}
class Restaurant{
    private String name;
    private String owner;
    private List<RestaurantBranch> branches;

    public Restaurant(String name, String owner) {
        this.name = name;
        this.owner = owner;
        branches = new ArrayList<>();
    }
    public void addBranches(RestaurantBranch branch){
        branches.add(branch);
    }

    public String getName() {
        return name;
    }
}

class RestaurantBranch{
    private int id;
    private String name;
    private Address address;
    private RestaurantManager manager;
    private RestaurantMenu menu;

    public RestaurantBranch(int id, String name, Address address, RestaurantManager manager, RestaurantMenu menu) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.manager = manager;
        this.menu = menu;
    }

    public RestaurantMenu getMenu() {
        return this.menu;
    }
}

class RestaurantManager extends User{
    public RestaurantManager(String email, String contactNumber) {
        super(email, contactNumber);
    }

    public void addFoodItemToMenu(RestaurantMenu menu, FoodItem foodItem){
        menu.addItem(foodItem);
    }
    public void removeFoodItemFromMenu(RestaurantMenu menu,FoodItem foodItem){
        if(menu.checkItemAvailability(foodItem)){
            menu.removeItem(foodItem);
        }
    }
    public void updateMenuItemPrice(RestaurantMenu menu,FoodItem foodItem,float Price){
        if(menu.checkItemAvailability(foodItem)){
            foodItem.setPrice(Price);
        }

    }
}
class RestaurantMenu{
    private int menuID;
    List<FoodItem> menuItems = new ArrayList<>();

    public   void addItem(FoodItem foodItem){
        menuItems.add(foodItem);
    }
    public boolean checkItemAvailability(FoodItem foodItem){
        return menuItems.contains(foodItem);
    }
    public void removeItem(FoodItem foodItem){
        menuItems.remove(foodItem);
    }
    public void showMenuItems(){
        for (int i = 0; i < menuItems.size(); i++) {
            FoodItem item = menuItems.get(i);
            System.out.println((i+1) + ". " + item.getName() + " - ₹" + item.getPrice());
        }
    }

}

class FoodItem{
    private String name;
    private String description;
    private boolean availability;
    private int quantity;
    private float price;

    public FoodItem(String name, String description, boolean availability, int quantity, float price) {
        this.name = name;
        this.description = description;
        this.availability = availability;
        this.quantity = quantity;
        this.price = price;
    }

    public boolean isAvailability() {
        return availability;
    }

    public int getQuantity() {
        return quantity;
    }

    public float getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public void updateFoodItemQuantity(int orderedQuantity){
        int remainingQuantity = quantity - orderedQuantity;
        if(remainingQuantity>0){
            quantity = remainingQuantity;
        }else{
            quantity=0;
            availability=false;
        }
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public boolean checkAvailability(){
        return availability;
    }

}
enum OrderStatus {
    PLACED, DELIVERED, CANCELLED
}
 enum PaymentStatus {
    PENDING, REFUNDED,COMPLETED
}

class Order{
  private int id;
  RestaurantBranch branch;
  List<OrderItem> orderItems;
  LocalDateTime orderedDate;
  private OrderStatus orderStatus;
  private PaymentStatus paymentStatus;
  private float orderTotal;


    public Order(int id, LocalDateTime orderedDate,RestaurantBranch branch) {
        this.id = id;
        this.orderedDate = orderedDate;
        this.branch=branch;
        orderItems = new ArrayList<>();
        this.orderStatus=OrderStatus.PLACED;
        this.paymentStatus=PaymentStatus.PENDING;

    }

    public void setOrderTotal(float orderTotal) {
        this.orderTotal = orderTotal;
    }

    public void addItemToOrderList(FoodItem foodItem, int quantity){
        orderItems.add(new OrderItem(foodItem,quantity));
    }

    public void calculateOrderTotal(){
        float price =0;
        for(OrderItem item : orderItems ){
            price += item.getPriceAtOrder()*item.getQuantity();
        }
        setOrderTotal(price);
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

}

class OrderItem{
    private String name;
    private float priceAtOrder;
    private int quantity;

    public OrderItem(FoodItem foodItem, int quantity) {
        this.name = foodItem.getName();
        this.priceAtOrder = foodItem.getPrice();
        this.quantity = quantity;
        foodItem.updateFoodItemQuantity(quantity);
    }

    public float getPriceAtOrder() {
        return priceAtOrder;
    }

    public int getQuantity() {
        return quantity;
    }
}

class DeliveryPerson extends User{
   LocalDate joinedDate ;
   int experience;
   Map<LocalDate,List<Order>> workhistory =new HashMap<>();
   int rating;

    public DeliveryPerson(String email, String contactNumber, LocalDate joinedDate, int experience, int rating) {
        super(email, contactNumber);
        this.joinedDate = joinedDate;
        this.experience = experience;
        this.rating = rating;
    }


}
