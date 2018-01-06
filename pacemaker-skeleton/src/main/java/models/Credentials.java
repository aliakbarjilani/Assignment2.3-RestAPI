package models;

import static com.google.common.base.MoreObjects.toStringHelper;

import java.io.Serializable;
import java.util.UUID;

import com.google.common.base.Objects;

public class Credentials implements Serializable {

  public String id;
  public String email;
  public String password;

  public Credentials() {
  }

  public String getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }
  
  public Credentials(String email, String password) {
    this.id = UUID.randomUUID().toString();
    this.email = email;
    this.password = password;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj instanceof Credentials) {
      final Credentials other = (Credentials) obj;
      return Objects.equal(email, other.email)
          && Objects.equal(password, other.password);
    } else {
      return false;
    }
  }

  @Override
  public String toString() {
    return toStringHelper(this).addValue(id)
        .addValue(email)
        .addValue(password)
        .toString();
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.id, this.email, this.password);
  }

}
