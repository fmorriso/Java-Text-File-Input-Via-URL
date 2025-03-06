class OneItem {

    // Add at least 3 attributes found in the data file
    private final String location;
    private final int year;
    private final String month;
    private final String period;
    private final String indicator;
    private final int dataValue;

    // Add a constructor that initializes the attributes
    public OneItem(String location, int year, String month) {
        this(location, year, month, "Monthly", "Number of", 0);
    }

    public OneItem(String location, int year, String month, String period, String indicator, int dataValue) {
        this.location = location;
        this.year = year;
        this.month = month;
        this.period = period;
        this.indicator = indicator;
        this.dataValue = dataValue;
    }

    // Add any getters and toString methods that you need
    public String getLocation() {return location;}
    public int getYear() {return year; }
    public String getMonth() {return month;}
    public String getBirthRate() {return period;}
    public String getIndicator() {return indicator;}
    public int getDataValue() {return dataValue;}

    @Override
    public String toString() {
        return new StringBuilder()
                .append("OneItem{")
                .append("location='").append(location)
                .append(", year='").append(year)
                .append(", month='").append(month)
                .append(", period='").append(period)
                .append(", indicator='").append(indicator)
                .append(", dataValue='").append(dataValue)
                .append('}').toString();
    }


}
