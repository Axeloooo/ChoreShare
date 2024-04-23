import { FlatList, StyleSheet, Text, View } from "react-native";
import chores from "../data/chores.json";

type Chore = {
  id: string;
  title: string;
  householdId: string;
  description: string;
  status: string;
  frequency: string;
  tag: string;
  userId: string;
  createdAt: Date;
  updatedAt: Date;
};

const Chores = () => {
  return (
    <View style={styles.container}>
      <FlatList
        data={chores}
        renderItem={({ item }) => (
          <View style={styles.item}>
            <Text>{item.title}</Text>
            <Text>{item.description}</Text>
            <Text>{item.status}</Text>
            <Text>{item.frequency}</Text>
            <Text>{item.tag}</Text>
          </View>
        )}
        keyExtractor={(item) => item.id}
      ></FlatList>
    </View>
  );
};

export default Chores;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    height: "100%",
  },
  item: {
    padding: 20,
    borderBottomWidth: 1,
    borderBottomColor: "#ccc",
  },
});
