public class QuantityMeasurementApp {

    // ---------------- ENUM FOR UNITS ----------------
    enum LengthUnit {
        FEET(1.0),
        INCH(1.0 / 12.0),
        YARDS(3.0),
        CENTIMETERS(0.0328084); // 1 cm = 0.0328084 feet

        private final double toFeetFactor;

        LengthUnit(double toFeetFactor) {
            this.toFeetFactor = toFeetFactor;
        }

        public double toFeet(double value) {
            return value * toFeetFactor;
        }
    }

    // ---------------- GENERIC QUANTITY CLASS ----------------
    static class Quantity {
        private final double value;
        private final LengthUnit unit;

        public Quantity(double value, LengthUnit unit) {
            if (unit == null) {
                throw new IllegalArgumentException("Unit cannot be null");
            }
            this.value = value;
            this.unit = unit;
        }

        private double toFeet() {
            return unit.toFeet(value);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;

            if (obj == null || getClass() != obj.getClass()) return false;

            Quantity other = (Quantity) obj;

            return Double.compare(this.toFeet(), other.toFeet()) == 0;
        }

        @Override
        public int hashCode() {
            return Double.hashCode(toFeet());
        }
    }

    // ---------------- MAIN METHOD ----------------
    public static void main(String[] args) {

        // Yard comparisons
        Quantity q1 = new Quantity(1.0, LengthUnit.YARDS);
        Quantity q2 = new Quantity(3.0, LengthUnit.FEET);
        Quantity q3 = new Quantity(36.0, LengthUnit.INCH);

        System.out.println("1 yard == 3 feet: " + q1.equals(q2));
        System.out.println("1 yard == 36 inches: " + q1.equals(q3));

        // Same unit
        Quantity q4 = new Quantity(2.0, LengthUnit.YARDS);
        Quantity q5 = new Quantity(2.0, LengthUnit.YARDS);

        System.out.println("2 yards == 2 yards: " + q4.equals(q5));

        // Centimeter comparisons
        Quantity q6 = new Quantity(1.0, LengthUnit.CENTIMETERS);
        Quantity q7 = new Quantity(0.393701, LengthUnit.INCH);

        System.out.println("1 cm == 0.393701 inches: " + q6.equals(q7));

        // Negative test
        Quantity q8 = new Quantity(1.0, LengthUnit.CENTIMETERS);
        Quantity q9 = new Quantity(1.0, LengthUnit.FEET);

        System.out.println("1 cm != 1 foot: " + q8.equals(q9));

        // Transitive property
        System.out.println("Transitive (yard == feet == inches): " +
                (q1.equals(q2) && q2.equals(q3) && q1.equals(q3)));

        // Edge cases
        System.out.println("Null comparison: " + q1.equals(null));
        System.out.println("Same reference: " + q1.equals(q1));

        try {
            Quantity invalid = new Quantity(1.0, null);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid unit handled: " + e.getMessage());
        }
    }
}