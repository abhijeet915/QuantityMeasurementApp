public class QuantityMeasurementApp {

    // ---------------- ENUM FOR UNITS ----------------
    enum LengthUnit {
        FEET(1.0),
        INCH(1.0 / 12.0); // 1 inch = 1/12 feet

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

        // Convert to base unit (feet)
        private double toFeet() {
            return unit.toFeet(value);
        }

        @Override
        public boolean equals(Object obj) {
            // Reflexive
            if (this == obj) return true;

            // Null & type check
            if (obj == null || getClass() != obj.getClass()) return false;

            Quantity other = (Quantity) obj;

            // Compare after conversion
            return Double.compare(this.toFeet(), other.toFeet()) == 0;
        }

        @Override
        public int hashCode() {
            return Double.hashCode(toFeet());
        }
    }

    // ---------------- MAIN METHOD ----------------
    public static void main(String[] args) {

        // Same unit equality
        Quantity q1 = new Quantity(1.0, LengthUnit.FEET);
        Quantity q2 = new Quantity(1.0, LengthUnit.FEET);

        // Cross-unit equality
        Quantity q3 = new Quantity(12.0, LengthUnit.INCH);

        // Different values
        Quantity q4 = new Quantity(2.0, LengthUnit.FEET);

        System.out.println("Feet vs Feet (1.0 == 1.0): " + q1.equals(q2));
        System.out.println("Feet vs Inches (1 ft == 12 in): " + q1.equals(q3));
        System.out.println("Different values (1 ft != 2 ft): " + q1.equals(q4));

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