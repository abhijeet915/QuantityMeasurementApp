public class QuantityMeasurementApp {

    // ---------------- ENUM ----------------
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
        public Quantity convertTo(LengthUnit targetUnit) {
            double convertedValue = convert(this.value, this.unit, targetUnit);
            return new Quantity(convertedValue, targetUnit);
        }

        public static double convert(double value, LengthUnit source, LengthUnit target) {
            validate(value, source);
            if (target == null) {
                throw new IllegalArgumentException("Target unit cannot be null");
            }

            double base = source.toFeet(value);
            return target.fromFeet(base);
        }

        private double toFeet() {
            return unit.toFeet(value);
        }

        // ---------------- ADDITION ----------------

        // Instance method (result in first operand's unit)
        public Quantity add(Quantity other) {
            if (other == null) {
                throw new IllegalArgumentException("Other quantity cannot be null");
            }

            double sumInFeet = this.toFeet() + other.toFeet();
            double resultValue = this.unit.fromFeet(sumInFeet);

            return new Quantity(resultValue, this.unit);
        }

        // Static method with explicit target unit
        public static Quantity add(Quantity q1, Quantity q2, LengthUnit targetUnit) {
            if (q1 == null || q2 == null) {
                throw new IllegalArgumentException("Operands cannot be null");
            }
            if (targetUnit == null) {
                throw new IllegalArgumentException("Target unit cannot be null");
            }

            double sumInFeet = q1.toFeet() + q2.toFeet();
            double resultValue = targetUnit.fromFeet(sumInFeet);

            return new Quantity(resultValue, targetUnit);
        }

        // Overloaded version (raw values)
        public static Quantity add(double v1, LengthUnit u1,
                                   double v2, LengthUnit u2,
                                   LengthUnit targetUnit) {

            return add(new Quantity(v1, u1), new Quantity(v2, u2), targetUnit);
        }

        // ---------------- EQUALITY ----------------
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

    // ---------------- MAIN METHOD ----------------
    public static void main(String[] args) {

        // Same unit
        System.out.println("1 ft + 2 ft = " +
                new Quantity(1.0, LengthUnit.FEET)
                        .add(new Quantity(2.0, LengthUnit.FEET)));

        // Cross unit
        System.out.println("1 ft + 12 in = " +
                new Quantity(1.0, LengthUnit.FEET)
                        .add(new Quantity(12.0, LengthUnit.INCHES)));

        System.out.println("12 in + 1 ft = " +
                new Quantity(12.0, LengthUnit.INCHES)
                        .add(new Quantity(1.0, LengthUnit.FEET)));

        // Yard + Feet
        System.out.println("1 yard + 3 ft = " +
                new Quantity(1.0, LengthUnit.YARDS)
                        .add(new Quantity(3.0, LengthUnit.FEET)));

        // CM + Inches
        System.out.println("2.54 cm + 1 in = " +
                new Quantity(2.54, LengthUnit.CENTIMETERS)
                        .add(new Quantity(1.0, LengthUnit.INCHES)));

        // Zero
        System.out.println("5 ft + 0 in = " +
                new Quantity(5.0, LengthUnit.FEET)
                        .add(new Quantity(0.0, LengthUnit.INCHES)));

        // Negative
        System.out.println("5 ft + (-2 ft) = " +
                new Quantity(5.0, LengthUnit.FEET)
                        .add(new Quantity(-2.0, LengthUnit.FEET)));

        // Static method with target unit
        System.out.println("1 ft + 12 in (inches) = " +
                Quantity.add(1.0, LengthUnit.FEET,
                        12.0, LengthUnit.INCHES,
                        LengthUnit.INCHES));
    }
}