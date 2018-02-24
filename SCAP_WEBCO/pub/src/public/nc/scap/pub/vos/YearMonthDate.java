package nc.scap.pub.vos;

import java.io.Serializable;
import java.util.StringTokenizer;
import nc.vo.ml.AbstractNCLangRes;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.lang.UFDate;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public final class YearMonthDate
  implements Serializable, Comparable<YearMonthDate>
{
  private static final long serialVersionUID = 201105281001000001L;
  private static final String SplitChar = "-";
  private String month;
  private String year;
  private String yearmonth;

  public YearMonthDate(String sdate)
  {
    StringTokenizer token = new StringTokenizer(sdate, "-");
    if (!token.hasMoreTokens()) {
//      ExceptionUtils.wrappBusinessException("日期格式异常");
    	 this.yearmonth=null;
    	 return;
    }
    this.year = token.nextToken().trim();
    this.month = token.nextToken().trim();
    if (this.month.length() == 1) {
      this.month = ("0" + this.month);
    }
    this.yearmonth = (this.year + "-" + this.month);
  }

  public YearMonthDate(String syear, String smonth) {
    this.year = syear.trim();
    this.month = smonth.trim();
    if (this.month.length() == 1) {
      this.month = ("0" + this.month);
    }
    this.yearmonth = (this.year + "-" + this.month);
  }

  public YearMonthDate(UFDate dt) {
    this(dt.toString());
  }

  public int compareTo(YearMonthDate o)
  {
    UFDate d1 = new UFDate(this.yearmonth + "-01");
    UFDate d2 = new UFDate(o.yearmonth + "-01");
    return d1.compareTo(d2);
  }

  public boolean isBefore(YearMonthDate o)
  {
    return compareTo(o) <= 0;
  }

  public boolean isAfter(YearMonthDate o)
  {
    return compareTo(o) >= 0;
  }

  public boolean equals(Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    YearMonthDate other = (YearMonthDate)obj;
    if (this.yearmonth == null) {
      if (other.yearmonth != null) {
        return false;
      }
    }
    else if (!this.yearmonth.equals(other.yearmonth)) {
      return false;
    }
    return true;
  }

  public YearMonthDate[] getBetweenYM(YearMonthDate endyear)
  {
    if (equals(endyear)) {
      return new YearMonthDate[] { this };
    }

    int months = (Integer.parseInt(endyear.getYear()) - Integer.parseInt(this.year)) * 12 + Integer.parseInt(endyear.getMonth()) - Integer.parseInt(this.month);

    if (months < 0) {
      return null;
    }
    YearMonthDate[] yms = new YearMonthDate[months];
    yms[0] = this;
    for (int i = 1; i < months; i++) {
      yms[i] = yms[(i - 1)].getNextMonth();
    }
    return yms;
  }

  public String getMonth() {
    return this.month;
  }

  public UFDate getMonthFirstDate()
  {
    UFDate dt = new UFDate(this.yearmonth + "-" + "01", true);

    return dt;
  }

  public UFDate getMonthLastDate()
  {
    UFDate dt = new UFDate(this.yearmonth + "-" + getMonthFirstDate().getDaysMonth(), false);

    return dt;
  }

  public YearMonthDate getNextMonth()
  {
    UFDate dt = getMonthFirstDate();

    int inextMonth = dt.getMonth() + 1;
    int iyear = dt.getYear();

    if (inextMonth > 12) {
      iyear += 1;
      inextMonth = 1;
    }
    return new YearMonthDate(String.valueOf(iyear), String.valueOf(inextMonth));
  }

  public YearMonthDate getPrevMonth()
  {
    UFDate dt = getMonthFirstDate();

    int iprevMonth = dt.getMonth() - 1;
    int iyear = dt.getYear();

    if (iprevMonth <= 0) {
      iyear -= 1;
      iprevMonth = 12;
    }
    return new YearMonthDate(String.valueOf(iyear), String.valueOf(iprevMonth));
  }

  public String getYear()
  {
    return this.year;
  }

  public String getYearmonth() {
    return this.yearmonth;
  }

  public int hashCode()
  {
    return this.yearmonth.hashCode();
  }

  public String toString()
  {
    return this.yearmonth;
  }
}