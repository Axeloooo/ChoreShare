import { createNativeStackNavigator } from "@react-navigation/native-stack";
import Chores from "../screens/Chores";

type ChoresStackParamList = {
  ChoresScreen: undefined;
};

const Stack = createNativeStackNavigator<ChoresStackParamList>();

const ChoresStackNavigator = () => {
  return (
    <Stack.Navigator initialRouteName="ChoresScreen">
      <Stack.Screen name="ChoresScreen" component={Chores}></Stack.Screen>
    </Stack.Navigator>
  );
};

export default ChoresStackNavigator;
