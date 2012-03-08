package org.hibernate.clementp;

import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Audited
public class Constant implements Serializable {
  @Id
  @Column(length = 3)
  public String id;

  @Column
  public String name;
}
