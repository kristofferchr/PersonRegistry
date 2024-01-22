export const nameRules = [
  v => !!v || 'Navn er påkrevd',
  v => /^[A-Z a-z]+$/.test(v) || 'Kun bokstaver som er gyldig',
]

export const ageRules = [
  v => !!v || 'Alder er påkrevd',
  v => /^\d+$/.test(v) || 'Kun tall er lovlig',
  v => {
    if (v >= 0 && v <= 200) return true;

    return 'Alder kan ikke være negativ og må være mindre enn 200';
  }
]
