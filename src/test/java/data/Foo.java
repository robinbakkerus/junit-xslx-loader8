package data;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Set;

import org.joda.time.LocalDate;

public class Foo {

	private String naam;
	private int simpleInt;
	private long simpleLong;
	private Integer objInt;
	private Long objLong;
	private boolean simpleBool;
	private Boolean objBool;
	private BigDecimal bigdec;
	private java.util.Date dateOfBirth;
	private LocalDate localdate;
	private FooType type;
	private Bar bar;
	private Collection<Bas> basColl;
	private Set<Bas> basSet;
	private Integer[] intArray;
	private String veryLongPropertyName;
	
	public String getNaam() {
		return naam;
	}
	public void setNaam(String naam) {
		this.naam = naam;
	}
	public int getSimpleInt() {
		return simpleInt;
	}
	public void setSimpleInt(int simpleInt) {
		this.simpleInt = simpleInt;
	}
	public long getSimpleLong() {
		return simpleLong;
	}
	public void setSimpleLong(long simpleLong) {
		this.simpleLong = simpleLong;
	}
	public Integer getObjInt() {
		return objInt;
	}
	public void setObjInt(Integer objInt) {
		this.objInt = objInt;
	}
	public Long getObjLong() {
		return objLong;
	}
	public void setObjLong(Long objLong) {
		this.objLong = objLong;
	}
	public boolean isSimpleBool() {
		return simpleBool;
	}
	public void setSimpleBool(boolean simpleBool) {
		this.simpleBool = simpleBool;
	}
	public Boolean getObjBool() {
		return objBool;
	}
	public void setObjBool(Boolean objBool) {
		this.objBool = objBool;
	}
	public BigDecimal getBigdec() {
		return bigdec;
	}
	public void setBigdec(BigDecimal bigdec) {
		this.bigdec = bigdec;
	}
	public java.util.Date getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(java.util.Date datum) {
		this.dateOfBirth = datum;
	}
	public LocalDate getLocaldate() {
		return localdate;
	}
	public void setLocaldate(LocalDate localdate) {
		this.localdate = localdate;
	}
	public FooType getType() {
		return type;
	}
	public void setType(FooType type) {
		this.type = type;
	}
	public Bar getBar() {
		return bar;
	}
	public void setBar(Bar bar) {
		this.bar = bar;
	}
	public Collection<Bas> getBasColl() {
		return basColl;
	}
	public void setBasColl(Collection<Bas> basColl) {
		this.basColl = basColl;
	}
	public Set<Bas> getBasSet() {
		return basSet;
	}
	public void setBasSet(Set<Bas> basSet) {
		this.basSet = basSet;
	}
	public Integer[] getIntArray() {
		return intArray;
	}
	public void setIntArray(Integer[] intArray) {
		this.intArray = intArray;
	}
	public String getVeryLongPropertyName() {
		return veryLongPropertyName;
	}
	public void setVeryLongPropertyName(String veryLongPropertyName) {
		this.veryLongPropertyName = veryLongPropertyName;
	}
	
	
}
