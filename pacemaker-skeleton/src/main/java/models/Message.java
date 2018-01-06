package models;

import static com.google.common.base.MoreObjects.toStringHelper;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.google.common.base.Objects;

public class Message implements Serializable {

  public String id;
  public String sender;
  public String recipient;
  public String message;
  public String reserved;

  //public Map<String, Activity> activities = new HashMap<>();

  public Message() {
  }

  public String getId() {
    return id;
  }

  public String getSender() {
    return sender;
  }

  public String getRecipient() {
    return recipient;
  }

  public String getMessage() {
    return message;
  }

  public String getReserved() {
    return reserved;
  }

  public Message(String sender, String recipient, String message, String reserved) {
    this.id = UUID.randomUUID().toString();
    this.sender = sender;
    this.recipient = recipient;
    this.message = message;
    this.reserved = reserved;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj instanceof Message) {
      final Message other = (Message) obj;
      return Objects.equal(sender, other.sender)
          && Objects.equal(recipient, other.recipient)
          && Objects.equal(message, other.message)
          && Objects.equal(reserved, other.reserved);
          //&& Objects.equal(activities, other.activities);
    } else {
      return false;
    }
  }

  @Override
  public String toString() {
    return toStringHelper(this).addValue(id)
        .addValue(sender)
        .addValue(recipient)
        .addValue(message)
        .addValue(reserved)
        //.addValue(activities)
        .toString();
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.id, this.sender, this.recipient, this.message, this.reserved);
  }
}