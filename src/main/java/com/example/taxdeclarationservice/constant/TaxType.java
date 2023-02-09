package com.example.taxdeclarationservice.constant;

public enum TaxType {
    VAT {
        @Override
        public double value() {
            return 19.5;
        }
    },
    POS {
        @Override
        public double value() {
            return 33;
        }
    },
    PO {
        @Override
        public double value() {
            return 10.5;
        }
    },
    FAD {
        @Override
        public double value() {
            return 5;
        }
    },
    SS {
        @Override
        public double value() {
            return 9.0;
        }
    },
    LR {
        @Override
        public double value() {
            return 12.4;
        }
    },
    REC {
        @Override
        public double value() {
            return 33.33;
        }
    },
    JA {
        @Override
        public double value() {
            return 35.6;
        }
    },
    ST {
        @Override
        public double value() {
            return 12.9;
        }
    };

    public abstract double value();

}
