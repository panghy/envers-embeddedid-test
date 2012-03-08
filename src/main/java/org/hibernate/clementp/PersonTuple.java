package org.hibernate.clementp;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Audited
public class PersonTuple implements Serializable {

  @Embeddable
  public static class PersonTupleId implements Serializable {
    @Column(nullable = false)
    public long personAId;
    @Column(nullable = false)
    public long personBId;
    @Column(nullable = false)
    public String constantId;

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      PersonTupleId personTupleId = (PersonTupleId) o;

      if (personAId == 0 || personBId == 0) throw new IllegalStateException();
      return personAId == personTupleId.personAId && personBId == personTupleId.personBId && !(constantId != null ? !constantId.equals(personTupleId.constantId) : personTupleId.constantId != null);
    }

    @Override
    public int hashCode() {
      if (personAId == 0 || personBId == 0) throw new IllegalStateException();
      int result = (int) (personAId ^ (personAId >>> 32));
      result = 31 * result + (int) (personBId ^ (personBId >>> 32));
      result = 31 * result + (constantId != null ? constantId.hashCode() : 0);
      return result;
    }
  }

  public PersonTuple() {
  }

  public PersonTuple(boolean helloWorld, Person personA, Person personB, Constant constant) {
    this.helloWorld = helloWorld;
    this.personA = personA;
    this.personB = personB;
    this.constant = constant;

    this.personTupleId.personAId = personA.id;
    this.personTupleId.personBId = personB.id;
    this.personTupleId.constantId = constant.id;

    personA.personATuples.add(this);
    personB.personBTuples.add(this);
  }

  @EmbeddedId
  private PersonTupleId personTupleId = new PersonTupleId();

  @MapsId("personAId")
  @ManyToOne(optional = false)
  @JoinColumn(insertable = false, updatable = false)
  public Person personA;

  @MapsId("personBId")
  @ManyToOne(optional = false)
  @JoinColumn(insertable = false, updatable = false)
  public Person personB;

  @MapsId("constantId")
  @ManyToOne(optional = false)
  @JoinColumn(insertable = false, updatable = false)
  public Constant constant;

  @Column(nullable = false)
  public boolean helloWorld = false;

  @Version
  @Column(name = "OPTLOCK", nullable = false)
  public long version;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    PersonTuple that = (PersonTuple) o;

    return personTupleId.equals(that.personTupleId);
  }

  @Override
  public int hashCode() {
    return personTupleId.hashCode();
  }
}
