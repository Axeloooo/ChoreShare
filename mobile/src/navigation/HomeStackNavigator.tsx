import { createNativeStackNavigator } from "@react-navigation/native-stack";
import Home from "../screens/Home";

type HomeStackParamList = {
  HomeScreen: undefined;
};

const Stack = createNativeStackNavigator<HomeStackParamList>();

const HomeStackNavigator = () => {
  return (
    <Stack.Navigator initialRouteName="HomeScreen">
      <Stack.Screen name="HomeScreen" component={Home}></Stack.Screen>
    </Stack.Navigator>
  );
};

export default HomeStackNavigator;
