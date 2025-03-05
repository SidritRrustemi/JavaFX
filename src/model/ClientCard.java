package model;
import java.io.Serializable;
import java.util.UUID;

public class ClientCard implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8244175544185194458L;
	private String clientCode;
	private String clientName;
	private String clientSurname;
	private String clientPhoneNr;
	private int points;
	
    public ClientCard(String clientName, String clientSurname, String clientPhoneNr) {
        if (clientName == null || clientName.trim().isEmpty()) {
            throw new IllegalArgumentException("Client name cannot be null or empty.");
        }
        if (clientSurname == null || clientSurname.trim().isEmpty()) {
            throw new IllegalArgumentException("Client surname cannot be null or empty.");
        }
        if (clientPhoneNr == null || clientPhoneNr.trim().isEmpty()) {
            throw new IllegalArgumentException("Client phone number cannot be null or empty.");
        }

        this.clientCode = UUID.randomUUID().toString();
        this.clientName = clientName;
        this.clientSurname = clientSurname;
        this.clientPhoneNr = clientPhoneNr;
        this.points = 0;
    }
	
    public String getClientCode() {
		return clientCode.toString();
	}
    
    public String getClientName() {
		return this.clientName;
	}
    
    public String getClientSurname() {
		return this.clientSurname;
	}
    
    public String getClientPhoneNr() {
		return this.clientPhoneNr;
	}
	
	public int getPoints() {
		return points;
	}
	
	public void addPoints(int points) {
        if (points <= 0) {
            throw new IllegalArgumentException("Points to add must be greater than zero.");
        }
        this.points += points;
	}
	
	public void subtractPoints(int points) {
        if (points <= 0) {
            throw new IllegalArgumentException("Points to subtract must be greater than zero.");
        }
        if (this.points < points) {
            throw new IllegalStateException("Not enough points to subtract. Current points: " + this.points);
        }
        this.points -= points;
	}
	
	@Override
	public String toString() {
		return "Client Code: " + this.getClientCode() + "/nClient name: " + this.clientName
				+ "\nClient surname: " + this.clientSurname + "\nClient Phone Number: "
				+ this.clientPhoneNr + "\nCurrent points: " + this.getPoints();
	}
	
}
