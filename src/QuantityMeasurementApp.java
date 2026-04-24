import java.util.Objects;

enum WeightUnit {
    KILOGRAM(1.0),
    GRAM(0.001),
    POUND(0.453592);

    private final double toKgFactor;

    WeightUnit(double toKgFactor) {
        this.toKgFactor = toKgFactor;
    }

    public double getConversionFactor() {
        return toKgFactor;
    }

    // convert value in this unit -> kilograms (base unit)
    public double convertToBaseUnit(double value) {
        return value * toKgFactor;
    }

    // convert value from kilograms -> this unit
    public double convertFromBaseUnit(double baseValue) {
        return baseValue / toKgFactor;
    }
}

final class QuantityWeight {
    private final double value;
    private final WeightUnit unit;

    public QuantityWeight(double value, WeightUnit unit) {
        if (unit == null || !Double.isFinite(value)) {
            throw new IllegalArgumentException("Invalid value or unit");
        }
        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public WeightUnit getUnit() {
        return unit;
    }

    public QuantityWeight convertTo(WeightUnit targetUnit) {
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }

        double base = unit.convertToBaseUnit(value); // kg
        double converted = targetUnit.convertFromBaseUnit(base);

        return new QuantityWeight(converted, targetUnit);
    }

    public QuantityWeight add(QuantityWeight other) {
        return add(this, other, this.unit);
    }

    public QuantityWeight add(QuantityWeight other, WeightUnit targetUnit) {
        return add(this, other, targetUnit);
    }

    public static QuantityWeight add(QuantityWeight w1, QuantityWeight w2, WeightUnit targetUnit) {
        if (w1 == null || w2 == null || targetUnit == null) {
            throw new IllegalArgumentException("Null values not allowed");
        }

        double baseSum =
                w1.unit.convertToBaseUnit(w1.value) +
                        w2.unit.convertToBaseUnit(w2.value);

        double result = targetUnit.convertFromBaseUnit(baseSum);

        return new QuantityWeight(result, targetUnit);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof QuantityWeight other)) return false;

        double thisBase = this.unit.convertToBaseUnit(this.value);
        double otherBase = other.unit.convertToBaseUnit(other.value);

        return Math.abs(thisBase - otherBase) < 1e-6;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Math.round(unit.convertToBaseUnit(value) * 1e6));
    }

    @Override
    public String toString() {
        return "QuantityWeight(" + value + ", " + unit + ")";
    }
}

public class QuantityMeasurementApp {

    public static void main(String[] args) {

        QuantityWeight kg = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        QuantityWeight gram = new QuantityWeight(1000.0, WeightUnit.GRAM);
        QuantityWeight pound = new QuantityWeight(2.20462, WeightUnit.POUND);

        // Equality
        System.out.println(kg.equals(gram)); // true
        System.out.println(kg.equals(pound)); // true (approx)

        // Conversion
        System.out.println(kg.convertTo(WeightUnit.GRAM)); // 1000 g
        System.out.println(pound.convertTo(WeightUnit.KILOGRAM));

        // Addition (default unit = first operand unit)
        System.out.println(
                new QuantityWeight(1.0, WeightUnit.KILOGRAM)
                        .add(new QuantityWeight(1000.0, WeightUnit.GRAM))
        );

        // Addition (explicit target unit)
        System.out.println(
                QuantityWeight.add(
                        new QuantityWeight(1.0, WeightUnit.KILOGRAM),
                        new QuantityWeight(1000.0, WeightUnit.GRAM),
                        WeightUnit.GRAM
                )
        );
    }
}