
export interface Person {
  Name :string
  Age: string
}

export const getOriginalPersons = (): Array<Person> => {
  return [{Name: "Kristoffer", Age: "30"}]
}
