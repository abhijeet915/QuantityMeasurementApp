public class QuantityMeasurementApp {

    // ---------------- ENUM FOR UNITS ----------------
    enum LengthUnit {
        FEET(1.0),
        INCHES(1.0 / 12.0),
        YARDS(3.0),
        CENTIMETERS(0.0328084);

        private final double toFeetFactor;

        LengthUnit(double toFeetFactor) {
            this.toFeetFactor = toFeetFactor;
        }

        public double toFeet(double value) {
            return value * toFeetFactor;
        }

        public double fromFeet(double feetValue) {
            return feetValue / toFeetFactor;
        }
    }

    // ---------------- QUANTITY CLASS ----------------
    static class Quantity {
        private final double value;
        private final LengthUnit unit;

        public Quantity(double value, LengthUnit unit) {
            validate(value, unit);
            this.value = value;
            this.unit = unit;
        }

        // ---------------- VALIDATION ----------------
        private static void validate(double value, LengthUnit unit) {
            if (unit == null) {
                throw new IllegalArgumentException("Unit cannot be null");
            }
            if (!Double.isFinite(value)) {
                throw new IllegalArgumentException("Value must be finite");
            }
        }

        // ---------------- CONVERSION ----------------

        // Instance method → returns new Quantity
        public Quantity convertTo(LengthUnit targetUnit) {
            double convertedValue = convert(this.value, this.unit, targetUnit);
            return new Quantity(convertedValue, targetUnit);
        }

        // Static API method
        public static double convert(double value, LengthUnit source, LengthUnit target) {
            validate(value, source);
            if (target == null) {
                throw new IllegalArgumentException("Target unit cannot be null");
            }

            // Normalize to base unit (feet)
            double valueInFeet = source.toFeet(value);

            // Convert to target
            return target.fromFeet(valueInFeet);
        }

        // ---------------- EQUALITY ----------------
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

        @Override
        public String toString() {
            return value + " " + unit;
        }
    }

    // ---------------- DEMO METHODS ----------------

    // Method 1: raw values
    public static void demonstrateLengthConversion(double value, LengthUnit from, LengthUnit to) {
        double result = Quantity.convert(value, from, to);
        System.out.println("Convert " + value + " " + from + " to " + to + " = " + result);
    }

    // Method 2: using Quantity object
    public static void demonstrateLengthConversion(Quantity quantity, LengthUnit to) {
        Quantity converted = quantity.convertTo(to);
        System.out.println("Convert " + quantity + " to " + to + " = " + converted.value);
    }

    public static void demonstrateLengthEquality(Quantity q1, Quantity q2) {
        System.out.println(q1 + " == " + q2 + " : " + q1.equals(q2));
    }

    public static void demonstrateLengthComparison(double v1, LengthUnit u1, double v2, LengthUnit u2) {
        Quantity q1 = new Quantity(v1, u1);
        Quantity q2 = new Quantity(v2, u2);
        demonstrateLengthEquality(q1, q2);
    }

    // ---------------- MAIN METHOD ----------------
    public static void main(String[] args) {

        // Basic conversions
        demonstrateLengthConversion(1.0, LengthUnit.FEET, LengthUnit.INCHES);
        demonstrateLengthConversion(3.0, LengthUnit.YARDS, LengthUnit.FEET);
        demonstrateLengthConversion(36.0, LengthUnit.INCHES, LengthUnit.YARDS);
        demonstrateLengthConversion(1.0, LengthUnit.CENTIMETERS, LengthUnit.INCHES);

        // Using Quantity object
        Quantity q = new Quantity(2.0, LengthUnit.YARDS);
        demonstrateLengthConversion(q, LengthUnit.INCHES);

        // Equality checks
        demonstrateLengthComparison(1.0, LengthUnit.FEET, 12.0, LengthUnit.INCHES);
        demonstrateLengthComparison(1.0, LengthUnit.YARDS, 3.0, LengthUnit.FEET);

        // Edge cases
        demonstrateLengthConversion(0.0, LengthUnit.FEET, LengthUnit.INCHES);
        demonstrateLengthConversion(-1.0, LengthUnit.FEET, LengthUnit.INCHES);

        // Round-trip check
        double original = 5.0;
        double converted = Quantity.convert(original, LengthUnit.FEET, LengthUnit.INCHES);
        double back = Quantity.convert(converted, LengthUnit.INCHES, LengthUnit.FEET);

        System.out.println("Round-trip (5 ft -> in -> ft): " + back);
    }
}