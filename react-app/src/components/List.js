import React from 'react';
import PropTypes from 'prop-types';

const PRODUCTS = [
  {
    category: 'Sporting Goods',
    price: '$49.99',
    stocked: true,
    name: 'Football',
  },
  {category: 'Sporting Goods', price: '$9.99', stocked: true, name: 'Baseball'},
  {
    category: 'Sporting Goods',
    price: '$29.99',
    stocked: false,
    name: 'Basketball',
  },
  {category: 'Electronics', price: '$99.99', stocked: true, name: 'iPod Touch'},
  {category: 'Electronics', price: '$399.99', stocked: false, name: 'iPhone 5'},
  {category: 'Electronics', price: '$199.99', stocked: true, name: 'Nexus 7'},
];

function ProductCategoryRow(props) {
  const {category} = props;
  return (
    <tr>
      <th colSpan="2">
        {category}
      </th>
    </tr>
  );
}

function ProductRow(props) {
  const {product} = props;
  const name = product.stocked ?
      product.name : (
        <span style={{color: 'red'}}>
          {product.name}
        </span>
      );
  return (
    <tr>
      <td>{name}</td>
      <td>{product.price}</td>
    </tr>
  );
}

class ProductTable extends React.Component {
  constructor(props) {
    super(props);
    this.handleProductChange = this.handleProductChange.bind(this);
    this.handlePriceChange = this.handlePriceChange.bind(this);
    this.handleAddProduct = this.handleAddProduct.bind(this);
    this.state = {
      product: null,
      price: null,
    };
  }

  handleProductChange(e) {
    this.setState({ product: e.target.value });
  }

  handlePriceChange(e) {
    this.setState({ price: e.target.value });
    e.target.value = null;
  }

  handleAddProduct() {
    const { product, price } = this.state;
    this.props.onAddProduct({ category: 'Other', name: product, price, stocked: true });
    this.setState({ product: '', price: '' });
  }

  render() {
    const {filterText} = this.props;
    const {inStockOnly} = this.props;

    const rows = [];
    let lastCategory = null;

    this.props.products.forEach((product) => {
      if (product.name.indexOf(filterText) === -1) {
        return;
      }
      if (inStockOnly && !product.stocked) {
        return;
      }
      if (product.category !== lastCategory) {
        rows.push(
          <ProductCategoryRow
            category={product.category}
            key={product.category}
          />,
        );
      }
      rows.push(
        <ProductRow
          product={product}
          key={product.name}
        />,
      );
      lastCategory = product.category;
    });
    return (
      <table>
        <thead>
          <tr>
            <th>Name</th>
            <th>Price</th>
          </tr>
        </thead>
        <tbody>{rows}</tbody>
        <tbody>
          <tr>
            <td>
              <input
                type="text"
                value={this.state.product}
                onChange={this.handleProductChange}
              />
            </td>
            <td>
              <input
                type="text"
                value={this.state.price}
                onChange={this.handlePriceChange}
              />
            </td>
            <td>
              <button type="button" onClick={this.handleAddProduct}>Add</button>
            </td>
          </tr>
,

        </tbody>
      </table>
    );
  }
}

class SearchBar extends React.Component {
  constructor(props) {
    super(props);
    this.handleFilterTextChange = this.handleFilterTextChange.bind(this);
    this.handleInStockChange = this.handleInStockChange.bind(this);
  }

  handleFilterTextChange(e) {
    this.props.onFilterTextChange(e.target.value);
  }

  handleInStockChange(e) {
    this.props.onInStockChange(e.target.checked);
  }

  render() {
    return (
      <form>
        <input
          type="text"
          placeholder="Search..."
          value={this.props.filterText}
          onChange={this.handleFilterTextChange}
        />
        <p>
          <input
            type="checkbox"
            checked={this.props.inStockOnly}
            onChange={this.handleInStockChange}
          />
          {' '}
            Only show products in stock
        </p>
      </form>
    );
  }
}

class FilterableProductTable extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      filterText: '',
      inStockOnly: false,
      products: this.props.products,
    };

    this.handleFilterTextChange = this.handleFilterTextChange.bind(this);
    this.handleInStockChange = this.handleInStockChange.bind(this);
    this.onAddProduct = this.onAddProduct.bind(this);
  }

  onAddProduct(product) {
    this.setState((state) => {
      const products = [...state.products, product];
      return {
        products,
      };
    });
  }

  onDeleteProduct() {

  }

  handleFilterTextChange(filterText) {
    this.setState({
      filterText,
    });
  }

  handleInStockChange(inStockOnly) {
    this.setState({
      inStockOnly,
    })
  }

  render() {
    return (
      <div>
        <SearchBar
          filterText={this.state.filterText}
          inStockOnly={this.state.inStockOnly}
          onFilterTextChange={this.handleFilterTextChange}
          onInStockChange={this.handleInStockChange}
        />
        <ProductTable
          products={this.state.products}
          filterText={this.state.filterText}
          inStockOnly={this.state.inStockOnly}
          onAddProduct={this.onAddProduct}
        />
      </div>
    );
  }
}

export default () => {
  return (
    <FilterableProductTable products={PRODUCTS} />
  );
};

ProductRow.propTypes = {
  product: PropTypes.string.isRequired,
};

ProductCategoryRow.propTypes = {
  category: PropTypes.string.isRequired,
};

ProductTable.propTypes = {
  products: PropTypes.arrayOf(PropTypes.number).isRequired,
};