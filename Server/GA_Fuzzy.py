import random
from fuzzy_new import Trapezoid, area
import matplotlib.pyplot as plt

x_start = float(input())
x_finish = float(input())
individuals = int(input())
generations = int(input())
xxx = input()
vvv = input()
www = input()
out = 0
global x1, v1, w1, x2, v2, w2, x3, v3, w3, x4, v4, w4, x5, v5, w5
g = 9.8
m = 0.5
maxis = 0.01
r = 0.002
R = 0.03
dt = 0.01
T = 60
l = 0.5
F1 = -20
F2 = 20
k = 0.1
e = 0.000001
x0 = 0.2
v0 = 0
mamd = 4
space = 60


def f(x, v, w):
    a = (m * r * r * (g - w)) / (0.5 * (m * R * R + maxis * r * r) + (m + maxis) * r * r)
    if (x == R and v < 0) or (x == l and v > 0):
        v = -v * (1 - k)
    xnew = x + v * dt + 0.5 * dt ** 2 * a
    vnew = v + a * dt
    if xnew > l:
        xnew = l
    if xnew < R:
        xnew = R

    return xnew, vnew


def get_lst(lst):
    new_lst = []
    new_lst.append(lst[0])
    new_lst.append(new_lst[-1] + lst[1])
    new_lst.append(new_lst[-1] + lst[2])
    new_lst.append(new_lst[-1] + lst[3])
    return new_lst


class Controller:
    def __init__(self, x, v):
        global x1, v1, w1, x2, v2, w2, x3, v3, w3, x4, v4, w4, x5, v5, w5
        self.x = Trapezoid([x, 0.05, 0.008, 0.008, 0.019, 0])
        self.v = Trapezoid([v, 0.071, 0.0575, 0.0575, 0.15, 0])
        self.y1 = min([self.intersection(self.x.a, self.x.b, self.x.c, self.x.d,
                                         x1.a, x1.b, x1.c, x1.d),
                       self.intersection(self.v.a, self.v.b, self.v.c, self.v.d,
                                         v1.a, v1.b, v1.c, v1.d)])
        self.y2 = min([self.intersection(self.x.a, self.x.b, self.x.c, self.x.d,
                                         x2.a, x2.b, x2.c, x2.d),
                       self.intersection(self.v.a, self.v.b, self.v.c, self.v.d,
                                         v2.a, v2.b, v2.c, v2.d)])
        self.y3 = min([self.intersection(self.x.a, self.x.b, self.x.c, self.x.d,
                                         x3.a, x3.b, x3.c, x3.d),
                       self.intersection(self.v.a, self.v.b, self.v.c, self.v.d,
                                         v3.a, v3.b, v3.c, v3.d)])
        self.y4 = min([self.intersection(self.x.a, self.x.b, self.x.c, self.x.d,
                                         x4.a, x4.b, x4.c, x4.d),
                       self.intersection(self.v.a, self.v.b, self.v.c, self.v.d,
                                         v4.a, v4.b, v4.c, v4.d)])
        self.y5 = min([self.intersection(self.x.a, self.x.b, self.x.c, self.x.d,
                                         x5.a, x5.b, x5.c, x5.d),
                       self.intersection(self.v.a, self.v.b, self.v.c, self.v.d,
                                         v5.a, v5.b, v5.c, v5.d)])

    def cross_bool(self, b1, c1, b2, c2):
        if b1 > c1:
            b1, c1 = c1, b1
        if b2 > c2:
            b2, c2, = c2, b2
        if b1 > c2:
            b1, b2 = b2, b1
            c1, c2 = c2, c1
        if c1 < b2:
            return False
        else:
            return True

    def intersection(self, a1, b1, c1, d1, a2, b2, c2, d2):
        b = self.cross_bool(b1, c1, b2, c2)
        if b:
            return 1
        if b1 > c2:
            a1, a2 = a2, a1
            b1, b2 = b2, b1
            c1, c2 = c2, c1
            d1, d2 = d2, d1
        b = self.cross_bool(c1, d1, a2, b2)
        if not b:
            return 0
        x = (a2 * c1 - d1 * b2) / (a2 - b2 + c1 - d1)

        return (d1 - x) / (d1 - c1)

    def find_min_point(self, p_xv):
        return min(p_xv)

    def return_(self):
        return self.y1, self.y2, self.y3, self.y4, self.y5


class Const:
    dna_length = 14
    mutation_prob = 0.01

    x_length = 16
    x_count = 5
    x_min = 0
    x_max = 0.5

    v_length = 16
    v_count = 5
    v_min = 0.05
    v_max = 0.5

    w_length = 16
    w_count = 5
    w_min = 0
    w_max = 40


class Individ:
    def __init__(self):
        self.dna = ''
        self.the_best_fitness = 0
        self.xxx = []
        self.vvv = []
        self.www = []

        for _ in range(((Const.x_length * Const.x_count) + (Const.v_length * Const.v_count) + (
                Const.w_length * Const.w_count)) * 4):
            self.dna += str(random.randint(0, 1))

    # Выбор наилучшего из двух
    def best(self, other):
        if self.fitness() < other.fitness():
            return self
        return other

    # Скрещивание
    def crossing_over(self, other):
        ind1 = random.randint(0, Const.dna_length - 1)
        one_part = self.dna[:ind1]
        two_part = other.dna[ind1:]
        temp = Individ()
        temp.dna = ''.join(one_part + two_part)
        return temp

    # Мутация (замена случайного элемента)
    def mutation(self):
        for i in range(len(self.dna)):
            ind1 = random.random()
            if ind1 < Const.mutation_prob:
                if self.dna[i] == "0":
                    self.dna = self.dna[:i] + "1" + self.dna[i + 1:]
                else:
                    self.dna = self.dna[:i] + "0" + self.dna[i + 1:]

    def get_x(self, n):
        prev = (n - 1) * Const.x_length * 4
        slice_1 = self.dna[prev:prev + Const.x_length]
        slice_2 = self.dna[prev + Const.x_length:prev + (Const.x_length * 2)]
        slice_3 = self.dna[prev + Const.x_length + Const.x_length:prev + (Const.x_length * 3)]
        slice_4 = self.dna[prev + (Const.x_length * 3):prev + (Const.x_length * 4)]
        x_1 = Const.x_min + ((Const.x_max - Const.x_min) * (int(slice_1, 2) / (2 ** Const.x_length - 1)))
        x_2 = Const.x_min + ((Const.x_max - Const.x_min) * (int(slice_2, 2) / (2 ** Const.x_length - 1)))
        x_3 = Const.x_min + ((Const.x_max - Const.x_min) * (int(slice_3, 2) / (2 ** Const.x_length - 1)))
        x_4 = Const.x_min + ((Const.x_max - Const.x_min) * (int(slice_4, 2) / (2 ** Const.x_length - 1)))
        return x_1, x_2, x_3, x_4

    def get_v(self, n):
        prev = ((n - 1) * Const.v_length * 4) + (Const.x_length * Const.x_count * 4)
        slice_1 = self.dna[prev:prev + Const.v_length]
        slice_2 = self.dna[prev + Const.v_length:prev + (Const.v_length * 2)]
        slice_3 = self.dna[prev + Const.v_length + Const.v_length:prev + (Const.v_length * 3)]
        slice_4 = self.dna[prev + (Const.v_length * 3):prev + (Const.v_length * 4)]
        v_1 = Const.v_min + ((Const.v_max - Const.v_min) * (int(slice_1, 2) / (2 ** Const.v_length - 1)))
        v_2 = Const.v_min + ((Const.v_max - Const.v_min) * (int(slice_2, 2) / (2 ** Const.v_length - 1)))
        v_3 = Const.v_min + ((Const.v_max - Const.v_min) * (int(slice_3, 2) / (2 ** Const.v_length - 1)))
        v_4 = Const.v_min + ((Const.v_max - Const.v_min) * (int(slice_4, 2) / (2 ** Const.v_length - 1)))
        return v_1, v_2, v_3, v_4

    def get_w(self, n):
        prev = ((n - 1) * Const.w_length * 4) + (Const.x_length * Const.x_count * 4) + (
                Const.v_length * Const.v_count * 4)
        slice_1 = self.dna[prev:prev + Const.w_length]
        slice_2 = self.dna[prev + Const.w_length:prev + (Const.w_length * 2)]
        slice_3 = self.dna[prev + Const.w_length + Const.w_length:prev + (Const.w_length * 3)]
        slice_4 = self.dna[prev + (Const.w_length * 3):prev + (Const.w_length * 4)]
        w_1 = Const.w_min + ((Const.w_max - Const.w_min) * (int(slice_1, 2) / (2 ** Const.w_length - 1)))
        w_2 = Const.w_min + ((Const.w_max - Const.w_min) * (int(slice_2, 2) / (2 ** Const.w_length - 1)))
        w_3 = Const.w_min + ((Const.w_max - Const.w_min) * (int(slice_3, 2) / (2 ** Const.w_length - 1)))
        w_4 = Const.w_min + ((Const.w_max - Const.w_min) * (int(slice_4, 2) / (2 ** Const.w_length - 1)))
        return w_1, w_2, w_3, w_4

    # Поиск пригодности
    def calc_fitness(self):
        global x1, v1, w1, x2, v2, w2, x3, v3, w3, x4, v4, w4, x5, v5, w5
        x_lst_1 = get_lst(self.get_x(1))
        v_lst_1 = get_lst(self.get_v(1))
        w_lst_1 = get_lst(self.get_w(1))

        x_lst_2 = get_lst(self.get_x(2))
        v_lst_2 = get_lst(self.get_v(2))
        w_lst_2 = get_lst(self.get_w(2))

        x_lst_3 = get_lst(self.get_x(3))
        v_lst_3 = get_lst(self.get_v(3))
        w_lst_3 = get_lst(self.get_w(3))

        x_lst_4 = get_lst(self.get_x(4))
        v_lst_4 = get_lst(self.get_v(4))
        w_lst_4 = get_lst(self.get_w(4))

        x_lst_5 = get_lst(self.get_x(5))
        v_lst_5 = get_lst(self.get_v(5))
        w_lst_5 = get_lst(self.get_w(5))

        x1 = Trapezoid([*x_lst_1, 1])
        v1 = Trapezoid([*v_lst_1, 1])
        w1 = [*w_lst_1, 1]

        # 2 правило
        x2 = Trapezoid([*x_lst_2, 1])
        v2 = Trapezoid([*v_lst_2, 1])
        w2 = [*w_lst_2, 1]

        # 3 правило
        x3 = Trapezoid([*x_lst_3, 1])
        v3 = Trapezoid([*v_lst_3, 1])
        w3 = [*w_lst_3, 1]

        # 4 правило
        x4 = Trapezoid([*x_lst_4, 1])
        v4 = Trapezoid([*v_lst_4, 1])
        w4 = [*w_lst_4, 1]

        # 5 правило
        x5 = Trapezoid([*x_lst_5, 1])
        v5 = Trapezoid([*v_lst_5, 1])
        w5 = [*w_lst_5, 1]
        counter = 0
        x, v = x0, v0
        for i in range(3000):
            lst = []
            trap = Controller(x, v).return_()
            trunc1 = Trapezoid([w1[0], w1[1], w1[2], w1[3], trap[0]])
            trunc2 = Trapezoid([w2[0], w2[1], w2[2], w2[3], trap[1]])
            trunc3 = Trapezoid([w3[0], w3[1], w3[2], w3[3], trap[2]])
            trunc4 = Trapezoid([w4[0], w4[1], w4[2], w4[3], trap[3]])
            trunc5 = Trapezoid([w5[0], w5[1], w5[2], w5[3], trap[4]])
            lst.append(trunc1)
            lst.append(trunc2)
            lst.append(trunc3)
            lst.append(trunc4)
            lst.append(trunc5)
            w = area(lst)
            x, v = f(x, v, w)
            global out
            # print(x_start, x_start+0.02, x_finish, x_finish-0.02)
            # if (x_start >= x) and (x <= x_finish) and (x <= x_start+0.02) and (x >= x_finish - 0.02):
            if (x >= x_start) and (x <= x_finish):
                counter += 1
                self.xxx.append(x)
                self.vvv.append(v)
                self.www.append(w)
            out += 1
            # if out % 1000 == 0:
            #     print(out, counter)
        self.the_best_fitness = -counter

    def fitness(self):
        return self.the_best_fitness

    def get_rule(self):

        for i in range(1, Const.x_count + 1):
            print("x" + str(i) + " = " + str(get_lst(self.get_x(i)) + [1]))
            print("v" + str(i) + " = " + str(get_lst(self.get_v(i)) + [1]))
            print("w" + str(i) + " = " + str(get_lst(self.get_w(i)) + [1]))


if __name__ == '__main__':
    ind_lst = []
    new_generation_lst = []
    the_best_of_the_best = Individ()

    # Количество индивидов
    for i in range(individuals):
        ind_lst.append(Individ())
        ind_lst[-1].calc_fitness()
        # print("ляляля")
        if ind_lst[i].fitness() < the_best_of_the_best.fitness():
            the_best_of_the_best = ind_lst[i]

    # Количество поколений
    for i in range(generations):
        for j in range(individuals):
            mum1 = random.choice(ind_lst)
            mum_final = mum1.best(random.choice(ind_lst))

            dad1 = random.choice(ind_lst)
            dad_final = mum1.best(random.choice(ind_lst))

            child = mum_final.crossing_over(dad_final)
            child.mutation()
            child.calc_fitness()
            new_generation_lst.append(child)
        ind_lst = new_generation_lst
        for i in range(len(ind_lst)):
            if ind_lst[i].fitness() < the_best_of_the_best.fitness():
                the_best_of_the_best = ind_lst[i]
        new_generation_lst = []

    mx = -1
    mx_hex = -1
    mx_mx = ''
    # print(the_best_of_the_best.fitness())
    # print(the_best_of_the_best.xxx)
    # print(the_best_of_the_best.vvv)
    # print(the_best_of_the_best.www)
    the_best_of_the_best.get_rule()

    fig, ax = plt.subplots()

    ax.plot([i for i in range(len(the_best_of_the_best.xxx))], the_best_of_the_best.xxx)
    ax.scatter(0, l, alpha=0)
    ax.scatter(0, 0, alpha=0)
    ax.set_xlabel("t, s")
    ax.set_ylabel("x, m")
    plt.savefig(xxx)
    ax.clear()

    ax.plot([i for i in range(len(the_best_of_the_best.vvv))], the_best_of_the_best.vvv)
    ax.scatter(0, l, alpha=0)
    ax.scatter(0, 0, alpha=0)
    ax.set_xlabel("t, s")
    ax.set_ylabel("v, m/s")
    plt.savefig(vvv)
    ax.clear()

    ax.plot([i for i in range(len(the_best_of_the_best.www))], the_best_of_the_best.www)
    ax.scatter(0, l, alpha=0)
    ax.scatter(0, 0, alpha=0)
    ax.set_xlabel("t, s")
    ax.set_ylabel("w, m/s^2")
    plt.savefig(www)
    ax.clear()

