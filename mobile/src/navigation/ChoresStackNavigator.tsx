import { createNativeStackNavigator } from "@react-navigation/native-stack";
import Chores from "../screens/Chores";
import CreateChore from "../screens/CreateChore";

export type ChoresStackParamList = {
  ChoresScreen: undefined;
  ChoresCreateScreen: undefined;
};

const Stack = createNativeStackNavigator<ChoresStackParamList>();

const ChoresStackNavigator = () => {
  return (
    <Stack.Navigator initialRouteName="ChoresScreen">
      <Stack.Screen name="ChoresScreen" component={Chores}></Stack.Screen>
      <Stack.Screen
        name="ChoresCreateScreen"
        component={CreateChore}
      ></Stack.Screen>
    </Stack.Navigator>
  );
};

export default ChoresStackNavigator;
