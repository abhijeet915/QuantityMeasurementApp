public class QuantityMeasurementApp {

    // ===================== LENGTH UNIT (Standalone Enum) =====================
    public enum LengthUnit {

        FEET(1.0),
        INCHES(1.0 / 12),
        YARDS(3.0),
        CENTIMETERS(1.0 / 30.48);

        private final double toFeetFactor;

        LengthUnit(double toFeetFactor) {
            this.toFeetFactor = toFeetFactor;
        }

        // Convert this unit → FEET (base unit)
        public double convertToBaseUnit(double value) {
            return value * toFeetFactor;
        }

        // Convert FEET → this unit
        public double convertFromBaseUnit(double baseValue) {
            return baseValue / toFeetFactor;
        }
    }

    // ===================== QUANTITY LENGTH CLASS =====================
    public static class QuantityLength {

        private final double value;
        private final LengthUnit unit;

        public QuantityLength(double value, LengthUnit unit) {
            if (unit == null || Double.isNaN(value) || Double.isInfinite(value)) {
                throw new IllegalArgumentException("Invalid value or unit");
            }
            this.value = value;
            this.unit = unit;
        }

        // Convert to any unit
        public QuantityLength convertTo(LengthUnit targetUnit) {
            double baseValue = unit.convertToBaseUnit(value);
            double converted = targetUnit.convertFromBaseUnit(baseValue);
            return new QuantityLength(converted, targetUnit);
        }

        // UC6: default addition (result in first operand unit)
        public QuantityLength add(QuantityLength other) {
            return add(other, this.unit);
        }

        // UC7: addition with explicit target unit
        public QuantityLength add(QuantityLength other, LengthUnit targetUnit) {

            if (other == null || targetUnit == null) {
                throw new IllegalArgumentException("Null input not allowed");
            }

            double sumInFeet =
                    this.unit.convertToBaseUnit(this.value)
                            + other.unit.convertToBaseUnit(other.value);

            double result = targetUnit.convertFromBaseUnit(sumInFeet);

            return new QuantityLength(result, targetUnit);
        }

        // UC1–UC8 Equality
        @Override
        public boolean equals(Object obj) {

            if (this == obj) return true;
            if (!(obj instanceof QuantityLength)) return false;

            QuantityLength other = (QuantityLength) obj;

            double thisBase = this.unit.convertToBaseUnit(this.value);
            double otherBase = other.unit.convertToBaseUnit(other.value);

            return Double.compare(thisBase, otherBase) == 0;
        }

        @Override
        public String toString() {
            return "Quantity(" + value + ", " + unit + ")";
        }
    }

    // ===================== MAIN METHOD =====================
    public static void main(String[] args) {

        QuantityLength oneFoot = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength twelveInches = new QuantityLength(12.0, LengthUnit.INCHES);
        QuantityLength threeFeet = new QuantityLength(3.0, LengthUnit.FEET);

        // Equality check
        System.out.println(oneFoot.equals(twelveInches)); // true

        // Conversion
        System.out.println(oneFoot.convertTo(LengthUnit.INCHES)); // 12 inches

        // Addition UC6
        System.out.println(oneFoot.add(twelveInches)); // 2 feet

        // Addition UC7
        System.out.println(oneFoot.add(twelveInches, LengthUnit.YARDS)); // ~0.667 yards

        // Cross unit equality
        System.out.println(new QuantityLength(36.0, LengthUnit.INCHES)
                .equals(new QuantityLength(1.0, LengthUnit.YARDS))); // true
    }
}