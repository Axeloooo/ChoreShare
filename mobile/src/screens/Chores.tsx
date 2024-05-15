import { FlatList, Pressable, StyleSheet, Text, View } from "react-native";
import chores from "../data/chores.json";
import { Ionicons } from "@expo/vector-icons";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { ChoresStackParamList } from "../navigation/ChoresStackNavigator";

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

type Props = NativeStackScreenProps<ChoresStackParamList, "ChoresScreen">;

const Chores = ({ route, navigation }: Props): React.JSX.Element => {
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
      <Pressable onPress={() => navigation.navigate("ChoresCreateScreen")}>
        <Ionicons
          name="add-circle"
          size={50}
          color="#2f95dc"
          style={styles.btn}
        />
      </Pressable>
    </View>
  );
};

export default Chores;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
    height: "100%",
  },
  item: {
    borderBottomWidth: 1,
    borderBottomColor: "#ccc",
    paddingVertical: 10,
  },
  btn: {
    position: "absolute",
    right: 20,
    bottom: 20,
  },
});
