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

        private double toFeet() {
            return unit.toFeet(value);
        }

        // ---------------- PRIVATE UTILITY (UC7 KEY) ----------------
        private static Quantity addInternal(Quantity q1, Quantity q2, LengthUnit targetUnit) {

            double sumInFeet = q1.toFeet() + q2.toFeet();
            double resultValue = targetUnit.fromFeet(sumInFeet);

            return new Quantity(resultValue, targetUnit);
        }

        // ---------------- ADD METHODS ----------------

        // UC6: result in first operand's unit
        public Quantity add(Quantity other) {
            if (other == null) {
                throw new IllegalArgumentException("Other quantity cannot be null");
            }
            return addInternal(this, other, this.unit);
        }

        // UC7: explicit target unit
        public static Quantity add(Quantity q1, Quantity q2, LengthUnit targetUnit) {
            if (q1 == null || q2 == null) {
                throw new IllegalArgumentException("Operands cannot be null");
            }
            if (targetUnit == null) {
                throw new IllegalArgumentException("Target unit cannot be null");
            }

            return addInternal(q1, q2, targetUnit);
        }

        // Overloaded (raw values)
        public static Quantity add(double v1, LengthUnit u1,
                                   double v2, LengthUnit u2,
                                   LengthUnit targetUnit) {

            return add(new Quantity(v1, u1), new Quantity(v2, u2), targetUnit);
        }

        // ---------------- CONVERSION ----------------
        public Quantity convertTo(LengthUnit targetUnit) {
            double base = this.toFeet();
            double converted = targetUnit.fromFeet(base);
            return new Quantity(converted, targetUnit);
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

        Quantity q1 = new Quantity(1.0, LengthUnit.FEET);
        Quantity q2 = new Quantity(12.0, LengthUnit.INCHES);

        // Explicit target units
        System.out.println("Feet result: " +
                Quantity.add(q1, q2, LengthUnit.FEET));

        System.out.println("Inches result: " +
                Quantity.add(q1, q2, LengthUnit.INCHES));

        System.out.println("Yards result: " +
                Quantity.add(q1, q2, LengthUnit.YARDS));

        // Other combinations
        System.out.println("Yard + Feet → Yards: " +
                Quantity.add(new Quantity(1.0, LengthUnit.YARDS),
                        new Quantity(3.0, LengthUnit.FEET),
                        LengthUnit.YARDS));

        System.out.println("Inches + Yard → Feet: " +
                Quantity.add(new Quantity(36.0, LengthUnit.INCHES),
                        new Quantity(1.0, LengthUnit.YARDS),
                        LengthUnit.FEET));

        System.out.println("CM + Inch → CM: " +
                Quantity.add(new Quantity(2.54, LengthUnit.CENTIMETERS),
                        new Quantity(1.0, LengthUnit.INCHES),
                        LengthUnit.CENTIMETERS));

        // Edge cases
        System.out.println("Zero case: " +
                Quantity.add(new Quantity(5.0, LengthUnit.FEET),
                        new Quantity(0.0, LengthUnit.INCHES),
                        LengthUnit.YARDS));

        System.out.println("Negative case: " +
                Quantity.add(new Quantity(5.0, LengthUnit.FEET),
                        new Quantity(-2.0, LengthUnit.FEET),
                        LengthUnit.INCHES));
    }
}