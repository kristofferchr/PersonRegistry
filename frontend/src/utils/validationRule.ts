export const nameRules = [
  (v: any) => !!v || 'Navn er påkrevd',
  (v: any) => /^[A-Z a-z]+$/.test(v) || 'Kun bokstaver som er gyldig',
]

export const ageRules = [
  (v: any) => !!v || 'Alder er påkrevd',
  (v: any) => /^\d+$/.test(v) || 'Kun tall er lovlig',
  (v: any) => {
    if (v >= 0 && v <= 200) return true;

    return 'Alder kan ikke være negativ og må være mindre enn 200';
  }
]
